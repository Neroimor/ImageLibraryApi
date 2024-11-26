# Этап 1. Регистрация и Авторизация

Добавлены контроллеры и сервисы и репозиторий регистрации пользователей.

## Регистрация
Регистрация происходит из двух этапов
1) Отправка данных см. модель `RegisterUser`
2) Верификация через uid отправленный на почту
Авторизация работает по модели `LoginData`

## Запуск
Для запуска приложений требуется поставить и настроить конфигурационный файл:
```yml
spring:
  application:
    name: ImageLibrary
  datasource:
    url: jdbc:postgresql://localhost:5432/name
    username: postgres
    password: password
    driver-class-name: org.postgresql.Driver
  mail:
    host: smtp.yandex.ru
    port: 465
    username: email
    password: password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
          ssl:
            enable: true

jwt:
  secret: mySuperSecretKeydasdas122321123122132dagadfgg
  expiration: 3600000

logging:
  level:
    org.springframework.security: DEBUG
    org.springframework.web: DEBUG

settingsRegister:
  settingUsers:
    rolle: "USER"
    salt: "randomSaltValue"
  registerResponse:
    createdUser: "Пользователь успешно зарегистрирован"
    userFoundAndNotVerefied: "Пользователь найден, но не подтверждён"
    userFound: "Пользователь найден"
    userVerefied: "Пользователь подтверждён"
    errorUID: "Неверный код подтверждения"
    notcorrectPassword: "Пароль неверен"
    passwordDontMatch: "Пароли не совпадают"
  errorResponseServer:
    connectedDB: "Ошибка при подключении к базе данных"
    userNotFound: "Пользователь не найден"
    userPasswordNotCorrect: "Неверный пароль"
  loginMoment:
    unauthorized: "Неверный логин или пароль"
    userNotFound: "Пользоывткль не найден"
    userNotVerified: "Пользователь не верефицирован"

```
Далее надо создать таблицу в базе данных (используется PostgresSQL)
```sql
CREATE TABLE users(
  id SERIAL PRIMARY KEY,
  email VARCHAR(255),
  password VARCHAR(255),
  nickname VARCHAR(255),
  ROLE VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  verified BOOLEAN DEFAULT FALSE,
  codeuid VARCHAR(7)
);
```