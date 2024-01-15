Si desea que la aplicación inicie cuando arranque el sistema operativo es necesario crear un acceso directo de LauncherLop.bat en la 
siguiente ruta:

C:\ProgramData\Microsoft\Windows\Start Menu\Programs\StartUp

IMPORTANTE: tenga la aplicación en una ruta donde el usuario no necesite privilegios de administrador para ejecutarse o puede no funcionar bien

el sistema operativo pedirá privilegios de administrador, en el caso de no poder crear directamente aqui el acceso directo creelo en una 
ruta donde tenga acceso (el escritorio por ejemplo) tras esto mueva el acceso directo a la ruta anteriormente mencionada y compruebe
que al iniciar el sisetma operativo se ejecuta correctamente la aplicación

Si la aplicación no se habre o da error al abrir es posible que necesite instalar JavaJDK 

https://www.oracle.com/es/java/technologies/downloads/#jdk21-windows

jkd21 es con el que ha sido compilado esta version

tambien es recomendable tener intalado java para aplicaciones de escritorio

https://www.java.com/es/

Si la aplicación sigue sin abrir dando un error intente lo siguiente:

	Windows 10 y Windows 8
		1.En Buscar, busque "este equipo" y pulse boton derecho >> propiedades
		2.Haga clic en "configuración avanzada del sistema".
		3.Haga clic en Variables de entorno. En la sección Variables del sistema busque la variable de entorno PATH y selecciónela.
			 Haga clic en "Nueva"
		4.En "nombre de la variable" escriba _JAVA_OPTIONS y en el valor escriba -Xmx512M
		5.pulse en Aceptar, vuelva a pulsar en aceptar y reinicie el sistema operativo 