
## Configuracion para la base de datos.
Para configurar una base de datos diferente puedes crear un archivo llamado 

```
application-dev.properties
```
Agregar las credenciales correspondientes

```
DATABASE_URL=jdbc:mysql://localhost:3306/db_ecomerce
DB_USER=root 
DB_PASS=password
```
Para porder correr la app con esas configuraciones.
```
mvn spring-boot:run "-Dspring-boot.run.profiles=dev"  
```
O en su defecto. agregar varibles en su editor o sistema. 