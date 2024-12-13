# Этап 1. Регистрация и Авторизация

Добавлены контроллеры и сервисы и репозиторий регистрации пользователей.

## Регистрация
Регистрация происходит из двух этапов
1) Отправка данных см. модель `RegisterUser`
2) Верификация через uid отправленный на почту
## Меню Админа
Меню админа имеет функции: 
1) Удалить пользователя по email
2) Изменить роль
3) Редактировать данные пользователя (никнейм и пароль)
4) Получение результатов о числе пользователей (с пагинацией)
5) Поиск пользователя по email
## Редактирование профиля
Редактирование профиля имеет две основные функции
1) Изменение пароля
2) Изменение никнейма
## Тестинг
Тесты работают только после настройки конфигурационного файла.
Для этого настройте почту для smpt и базу данных
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

settings-register:
  setting-users:
    role: "USER"
    salt: "randomSaltValue"
  register-response:
    created-user: "Пользователь успешно зарегистрирован"
    user-found-and-not-verified: "Пользователь найден, но не подтверждён"
    user-found: "Пользователь найден"
    user-verified: "Пользователь подтверждён"
    error-uid: "Неверный код подтверждения"
    incorrect-password: "Пароль неверен"
    passwords-dont-match: "Пароли не совпадают"
    confirm-your-email: "Подтвеждение регистрации"
    confirm-your-email-subject: "Подтвеждение регистрации"

  error-response-server:
    connected-db: "Ошибка при подключении к базе данных"
    user-not-found: "Пользователь не найден"
    user-password-not-correct: "Неверный пароль"
  login-moment:
    unauthorized: "Неверный логин или пароль"
    user-not-found: "Пользователь не найден"
    user-not-verified: "Пользователь не верифицирован"
    
setting-admin:
  setting-operation:
    deleted: "Пользователь удален"
    edit: "Данные пользователя обнавлены"
    user-not-found: "Пользователь не найден"
  setting-role:
    role-admin: "ADMIN"
    role-user: "USER"
    user-change-role: "Роль сменена на "

editor-user:
  setting-profile:
    renickname: "Вы успешно сменили никнейм"
    user-error: "Пользователь не найден"
    validation-error: "Пожалуйста пройдите проверку"
    repassword: "Вы успешно сменили пароль"
    password-error: "Неверный пароль"
    error-verification: "Пользователь не верефицирован"
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