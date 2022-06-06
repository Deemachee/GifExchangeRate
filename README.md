Описание:
Создать сервис, который обращается к сервису курсов валют, и отображает gif:
если курс по отношению к USD за сегодня стал выше вчерашнего, то отдаем рандомную 
https://giphy.com/search/rich , если ниже, отсюда - https://giphy.com/search/broke

REST API курсов валют https://docs.openexchangerates.org/
REST API гифок https://developers.giphy.com/docs/api#quick-start-guide

# Must Have
Сервис на Spring Boot 2 + Java / Kotlin
Запросы приходят на HTTP endpoint (должен быть написан в соответствии с rest conventions), туда передается код валюты по отношению с которой сравнивается USD
Для взаимодействия с внешними сервисами используется Feign
Все параметры (валюта по отношению к которой смотрится курс, адреса внешних сервисов и т.д.) вынесены в настройки
На сервис написаны тесты (для мока внешних сервисов можно использовать @mockbean или WireMock)
Для сборки должен использоваться Gradle
Результатом выполнения должен быть репо на GitHub с инструкцией по запуску

# Nice to Have
Сборка и запуск Docker контейнера с этим сервисом

Stack:
- Java 11
- Spring Boot 
- Spring Cloud(OpenFeign)
- JUnit 5

Endpoint:
- Localhost:8080/get/{currency}      ,где currency - код валюты в международном
обозначении, пишется большими буквами. Пример:
  Localhost:8080/get/RUB