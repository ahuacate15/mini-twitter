<img src=".github/logo_mini_twitter.jpeg" height="110" />

## Acerca del proyecto
Proyecto basado en el curso de android del maestro **@miguelcamposdev**, desarrollado en JAVA para fines didacticos.

## Caracteristicas
* Inicia sesión o registrate en la aplicación
* Publica tweets de hasta 256 caracteres
* Elimina tweets
* Marca tweets favoritos
* Guarda y actualiza la información de tu perfil

## Herramientas utilizadas
* Consumo de servicios HTTP utilizando retrofit
* Parseo de datos JSON con volley
* Implementacion del LiveData
* Manejo de fragments
* Arquitectura MVVM

## Proximamente
* Sistema de hashtags
* Edita y comenta tweets
* Menciona usuarios
* Pruebas unitarias

## Instalación
Necesitas un servidor apache y un servidor mysql para correr la [API mini-twitter](https://github.com/ahuacate15/api-mini-twitter), baja el código fuente y sigue las instrucciones de instalación del mismo:
```
git clone https://github.com/ahuacate15/api-mini-twitter.git
```

Cuando la API se encuentre corriendo en tu equipo, agrega la **IP privada** de tu PC en el archivo ``com.carlos.minitwitter.common.Constant.java`` para que puedas probar la aplicación en tu red local.
```java
public class Constant {
    public static String API_URL = "http://YOUR_IP/api-mini-twitter/";
    ...
}
```
