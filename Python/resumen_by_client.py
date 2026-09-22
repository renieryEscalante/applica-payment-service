"""Genera un resumen de pagos por cliente a partir del endpoint GET /payments."""

import argparse
import base64
import csv
import json
import os
import sys
from collections import defaultdict
from decimal import Decimal, InvalidOperation
from pathlib import Path
from urllib.error import HTTPError, URLError
from urllib.request import Request, urlopen


DEFAULT_URL = "http://localhost:8080/applica-payment-service/payments"
DEFAULT_OUTPUT = "resumen_por_cliente.csv"


def get_payments(url, username, password):
    credentials = base64.b64encode(f"{username}:{password}".encode("utf-8")).decode("ascii")
    request = Request(
        url,
        headers={
            "Accept": "application/json",
            "Authorization": f"Basic {credentials}",
        },
        method="GET",
    )

    try:
        with urlopen(request, timeout=15) as response:
            payload = json.load(response)
    except HTTPError as error:
        if error.code in (401, 403):
            print("Error de autenticación: valide el usuario y la contraseña.", file=sys.stderr)
        else:
            print(f"La API respondió con el estado HTTP {error.code}.", file=sys.stderr)
        return None
    except URLError as error:
        print(f"No fue posible conectar con la API: {error.reason}.", file=sys.stderr)
        return None
    except TimeoutError:
        print("La conexión con la API excedió el tiempo de espera.", file=sys.stderr)
        return None
    except json.JSONDecodeError:
        print("La API no devolvió una respuesta JSON válida.", file=sys.stderr)
        return None

    if not isinstance(payload, list):
        print("La API no devolvió una lista de pagos.", file=sys.stderr)
        return None

    return payload


def summarize_by_customer(payments):
    summary = defaultdict(lambda: {"total": Decimal("0"), "count": 0})

    for payment in payments:
        try:
            customer_id = payment["customerId"]
            amount = Decimal(str(payment["amount"]))
        except (KeyError, InvalidOperation, TypeError):
            print(f"Pago omitido por datos inválidos: {payment}", file=sys.stderr)
            continue

        summary[customer_id]["total"] += amount
        summary[customer_id]["count"] += 1

    return summary


def write_csv(summary, output_file):
    try:
        output_path = Path(output_file)
        with output_path.open("w", newline="", encoding="utf-8") as csv_file:
            writer = csv.writer(csv_file)
            writer.writerow(["CUSTOMER_ID", "TOTAL_AMOUNT", "PAYMENT_COUNT", "AVERAGE_TICKET"])

            for customer_id, values in sorted(summary.items(), key=lambda item: str(item[0])):
                average = values["total"] / values["count"]
                writer.writerow(
                    [
                        customer_id,
                        f"{values['total']:.2f}",
                        values["count"],
                        f"{average:.2f}",
                    ]
                )
    except OSError as error:
        print(f"No fue posible crear el archivo CSV: {error}.", file=sys.stderr)
        return False

    print(f"Resumen generado en: {output_path.resolve()}")
    return True


def parse_arguments():
    parser = argparse.ArgumentParser(description="Genera un CSV con el resumen de pagos por cliente.")
    parser.add_argument("--url", default=os.getenv("PAYMENTS_API_URL", DEFAULT_URL), help="URL del endpoint GET /payments")
    parser.add_argument("--username", default=os.getenv("PAYMENTS_API_USER", "applica"), help="Usuario de Basic Authentication")
    parser.add_argument("--password", default=os.getenv("PAYMENTS_API_PASSWORD", "4pl1c42026!"), help="Contraseña de Basic Authentication")
    parser.add_argument("--output", default=DEFAULT_OUTPUT, help="Ruta del archivo CSV de salida")
    return parser.parse_args()


def main():
    args = parse_arguments()
    payments = get_payments(args.url, args.username, args.password)

    if payments is None:
        return 1

    return 0 if write_csv(summarize_by_customer(payments), args.output) else 1


if __name__ == "__main__":
    sys.exit(main())
