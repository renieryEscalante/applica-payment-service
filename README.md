# applica-payment-service
# Decisiones

## Guardado de XML en disco

El XML se guarda en disco para conservar evidencia visible de la conversión del request al formato XML. A nivel de código, este guardado también simula cómo se consumiría un cliente externo SOAP o RESTful.

## Protección de endpoints

Se utiliza Basic Authentication para proteger los endpoints, ya que no se dispone de otro endpoint para generar un JWT.

# Siguiente iteración

1. Incorporación de filtros y paginación en la consulta de los pagos procesados: **GET /payments**.
2. Creación de pruebas unitarias completas para toda la aplicación.

# Instrucciones de ejecución de proyecto

1. Descargue el repositorio desde GitHub.
2. Abra Spring Tools Suite (STS) y seleccione **File > Import > Maven > Existing Maven Projects**.
3. Seleccione la carpeta clonada del proyecto y confirme la importación.
4. Espere a que STS descargue las dependencias de Maven y finalice la compilación inicial.
5. En el explorador de proyectos, haga clic derecho sobre la clase `Application` y seleccione **Run As > Spring Boot App**.
