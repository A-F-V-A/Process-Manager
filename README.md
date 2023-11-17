# Process Manager

Process Manager es una aplicación Java diseñada para crear y administrar procesos complejos y sus respectivas actividades y tareas. Ideal para la gestión de proyectos y flujos de trabajo, facilita el seguimiento y la coordinación de diversas tareas y actividades.

## Comenzando

Estas instrucciones te permitirán obtener una copia del proyecto en funcionamiento en tu máquina local para propósitos de desarrollo y pruebas.

### Prerrequisitos

Necesitarás Java y Maven instalados en tu máquina. Asegúrate de tener las versiones más recientes para una experiencia óptima.

```bash
java -version
mvn -version
```

### Instalación

Sigue estos pasos para tener un entorno de desarrollo en ejecución:

```bash
# Clonar el repositorio
git clone https://github.com/tu-usuario/process-manager.git

# Entrar al directorio
cd process-manager

# Instalar dependencias
mvn install

# Ejecutar
java -jar target/process-manager-1.0.0.jar
```

## Uso

El sistema permite crear procesos, añadir actividades y tareas, y gestionar el flujo de trabajo:

```java
ProcessManager manager = ProcessManager.getInstance();
Process registrationProcess = new Process("Client Registration");
// Añadir actividades y tareas...
manager.addProcess(registrationProcess);
```

## Ejecutando las pruebas

Ejecuta las pruebas automatizadas para este sistema con:

```bash
mvn test
```

### Desglose de las pruebas end-to-end

Estas pruebas verifican que el flujo de trabajo del proceso, desde la creación hasta la finalización, funciona como se espera.

```bash
mvn test -Dtest=ProcessWorkflowTest
```

## Despliegue

Para desplegar este proyecto en un sistema en vivo, utiliza:

```bash
mvn package
java -jar target/process-manager.jar
```
