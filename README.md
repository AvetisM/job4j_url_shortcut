# job4j_url_shortcut

### Проект для изучения REST-сервисов.

##### job4j_url_shortcut это сервис для замены ссылок пользователей. Обеспечивает безопастность взаимодействия с различными сайтами.

### Описание api:
### - POST
`/api/v1/sites/registration - регистрирует сайт на сервисе.`
+ Входящие данные json вида {url : value}. 
+ Исходящие данные json вида {registration : true/false, login: unique_value, password : unique_value}.
  
`/login - Авторизация в сервисе.`
+ Входящие данные json вида {login: login, password : password}.
+ Исходящие данные ключ авторизации в заголовках

`/api/v1/sites/convert - преобразует адрес сайта и отправляеет код`
+ Входящие данные json вида {url : value}.
+ Исходящие данные json вида {code : unique_value}.

### - GET
`/api/v1/sites/redirect/{code} - переадресация по коду.`
+ {code} - значение ранее полученного кода.
+ Исходящие данные json вида {url : value}.

`/api/v1/sites/statistic - получение статистики по количеству вызовов для каждого сайта.`
+ Исходящие данные json вида {url : value, total : value}.

### Стек технологий:
+ Java 17
+ PostgresSql 14
+ Maven
+ SpringBoot
+ lombok 1.18.22

### Требования к окружению:
+ Java 17
+ Maven 3.x
+ PostgresSql 14

### Запуск приложения
+ Создать базу данных

```create database job4j_url_shortcut;```

+ Запуск приложения с maven. Перейдите в корень проекта через командную строку и выполните команды:

``` mvn clean install```
``` mvn spring-boot:run```
### 5. Контакты:
- **email**    avetis.mkhitaryants@gmail.com
- **телеграм** @avetis_m