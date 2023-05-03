### Riesgos:
1. Api de terceros y gratuita -> sin garantía que es estará disponible 24/7 + limitación en número de peticiones (x / día / mes / por segundo...).
### Posible solución:
1. Tener N Apis de backup. -> Implica: a más N, más flexible y abstracto tiene que ser el diseño  + poder canviar de api target sin tener que "tumbar" la aplicación.

### Requisitos y funcionalidades de las apis vistas:
1. Hay que proporcionarles un **código ejecutable**.
2. Hay que especificar la tecnología, con posibilidad de indicar la versión.
3. Se comprometen a devolver **"el resultado"**. En caso de error algunos pueden devolver el mensaje de la excepción.
4. **El resultado es siempre "lo que el codigo ejecutbale imprime por consola"**.
5. Algunas apis permiten proporcionar **input data(s)**. Estos **deben ser "leidos como si el usuario los introdujera por consola"**. Obs: ha visto mucho la palabra **STDIN**, no estoy seguro de lo que significa, pero me imagino que los imputs deben ser leidos por el ejecutable según el estandard del lenguaje.

### Diseño del prototipo implementado:
1. ApiTarget: Enum. Cada objeto representa una posible API a la que se podría llamar. Se puede usar como nuestro **índice de APIS**.
   - De momento solo una funcionando (prototipo, pero he visto más que probablemente se podrían añadir, links en ApiTarget).
2. ApiSpec: classe para almacenar la información de **cómo** establecer la conexión a una determinada API. Ir mejorando su dieño según surja la necesidad. Incluso podría llegar a ser una entidad persistente.
3. ApiSpecProvier: bean. Encargada de proveer un ApiSpec según el ApiTarget. De momento inicializa los ApiSpecs hardcoded,  pero se podrían leer de un archivo de configuración. Contiene un map <ApiTarget, ApiSpec> para asociar la info, xo se podría "transformar" en un repository de ApiSpec.
4. Proxy: Se le inyecta un ApiSpecProvider. 
   1. Método a invocar: **requestCodeExecution(apiTarget, apiTargetRequestBody)**.
   2. Internamente pide al ApiSpecProvier el ApiSpec associado al apiTarget (proporcionado en la petición).
   3. De esta manera, el webclient del proxy debería funcionar par cualquier api target.
   4. Si el diseño se complica: otra opción seía tener N web clients ya configurados, y que el proxy le pida a una factoría el webclient que se necesite segun el api target.
5. ApiRequestDto: puede llegar a ser la clase **más problemática**. Ya que cada API puede tener "sus peculiaridades" (algún campo extra, o con otro nombre...). **Diseño provisional, update on demand**.
6. ApiResponseDto: puede variar, pero con tal de poner tantos campos como sea necesario y así poder deserializar cualquier posible respuesta... Chapuzilla si, xo funcionaría, y la app, al saber la api target, podría hacer los gets "según el caso".

### Riesgo en el proceso de desarrollo:
1. **Automatizar la creación del campo del codigo ejecutable (para luego setearlo en el requestDto de la petición) tiene pinta de no ser trivial**
2. He hecho pruebas con distintos RequestDto (a puro dummy en un .json), para probar como debería ser el "String del codigo ejecutable".
      - En el último set de pruebas el dto de la petición tiene: 2 valores input + un codigo ejecutalbe que lee 2 valores + llama a una función que calcula la suma de estos valores (pasandolos como paramtros) + muestra el resultado -> el dto de respuesta nos da la suma. Done en PHP, Python, y java. En node no consigo lo de leer inputs por consola, demasiado complejo para mi, que no tengo ni idea de node (Questions, Promises... wtf): **help needed**) ** 
      - El set de pruebas previo es el mismo ejercico, pero en lugar de leer los inputs y guardarlos en variables, estas  estan inicializadas en el codigo proporcionado. Puede ser una opción viable, ya que los valores que se pasan como a argumento al método para validarlo pueden ser tratados como constantes (introducidos por el creador del reto).
      - Así que **implementar "un validador de métodos/funciones senzillas" es viable a corto plazo**. (aunque automatizarlo no es tarea fácil, no seran unas pocas semanas). 
      - Obs: Hay que imaginar que el alumno solo proporcionaría el método/función, la inclusión del resto del código lo tenemos que automatizar nosotros (y es distinto para cada tecnología).
      - Ver resources.json.requests + los últimos en examples + el java code.txt + los tests hechos.
3. **Pero ahora el principal riesgo está en como el cliente quiere que sean los retos (como se crean + que código tiene que introducir el alumno + que significa para el el concepto "solución" y como se valida)**
4. Ahora mismo **estamos "a ciegas"**. Personalmente yo opino:
   1. No esperar y atacar ya las historias de usuario 4.1 y 5.1 (ídem requisito: crear reto, lo del rol es secundario, cuando se implemente la seguridad se restinge el acceso y listos).
      - O al menos empezar ya las conversaciones para que el cliente sea un poco más específico respecto a los challenges. Al fin y al cabo es el core de lo que quiere.
      - Y mientras se hacen las gestiones terminar la épica 1, para que **tenga algo funcionando**, pero **sin modelos ni persistencia, tirando de dummys y dtos**. O como mucho un modelo básico de Challenge (no persistente + solo con la basic info del listado), para aplicarle a el (y no al dto) la lógica de filter y sorting.
   2. Postergarlo probablemente implique tener que refactorizar mucho más adelante, o incluso que **se haya gastado tiempo/dienero en implementar algo que no vale para nada** porque no se adapta a lo que pedirá (ej: sección de soluciones + sección enviar solución + modelado de challenge + ORM/ODM).
   3. **Es probable que ni el propio cliente sepa muy bien lo que quiere (sería lo normal).**
   4. Además es probable que no tenga conocimientos técnicos (probablemente sea alguien del staff/administración de ITA). Entonces:
      - Proponer al cliente empezar por algo "sencillo, bueno, bonito y barato", y ya más adelante ampliar funcionalidad -> retos que implquen que el alumno: 
        - codifique un método/función + validación: a tal juego de inputs se tiene que obtener tal juego de outpus".
        - codifique todo el script (leyendo N valores de entrada + método + imprimiendo por consola el resultado) -> en java requiere escribir toda la clase principal + el main + posibles imports// en node requiere leer stdin ...
      - Si lo aprueba: **hablar/requisitar con "el experto en el domino"**, es decir **con los mentores**. Dudo mucho que alguien sin conocimientos técnicos se dedique a crear retos (aunque quiera/debe tener la posibilidad de poder hacerlo). **Con mucha probabilidad quien use la funcionalidad de crear retos serán los mentores**. Así que deberían ser ellos quienes llevaran la voz cantante en cuanto a como la aplicación debe interactuar con el usuario para llevar a cabo la funcionalidad "crear reto".
      - Se podría asegurar al cliente que **el tendrá la última palabra**, pero que sería mejor **delegar en los mentores los aspectos técnicos de como debe introducirse y validarse un reto**.
      - Así: colaboraríamos mejor, ya que "hablaríamos el mismo idioma" + tendríamos una idea clara de lo que hay que hacer + evitaríamos que el cliente vaya "metiendo ruido y dando tumbos" (alguien no técnico se comportaría así, aunque no fuese esta su intención).
      - Y dudo mucho que al final el cliente pueda oponerse (aunque quisiera) a lo decidio, **ya que la mayoría de mentores (o cuantos más mejor) habrían dado "su opinión" (punto clave) y estarían de acuerdo en que lo implementado satisface sus necesidades para poder crear buenos retos para los alumnos (sencillos si, pero útiles para el user target inicial: alumnos empezando en alguna tecnología).**
      - Y ya una vez implementada y desplegada **toda** la app , se podría empezar la fase "ampliar funcionalidad con retos más complejos a gusto del cliente", sin temer "que el cliente se flipe y su implementación implique augmentar el plazo de entrega (y los costes)", ya que la app ya estaría 100% operativa y siendo usada (mucha menor presión para "cumplir el plazo de implementación")  ->  :)


