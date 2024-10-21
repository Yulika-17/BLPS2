# Рефакторинг баз данных и приложений  
## Сценарий 2: Рефакторинг существующего программного проекта

### Студент:
- Мальцева Юлия Игоревна

### Вариант №12106: [Федеральная налоговая служба России](http://www.nalog.ru)

### Задание:

Разработать приложение на базе Spring Boot, реализующее бизнес-процесс "Добавление и подтверждение платежа".

Приложение должно использовать СУБД PostgreSQL для хранения данных.

Для всех публичных интерфейсов должны быть разработаны REST API.

### Управление транзакциями:

Управление транзакциями необходимо реализовать с помощью Spring JTA.  
В реализованных прецедентах необходимо использовать декларативное управление транзакциями.

### Разграничение доступа:

Реализовать разграничение доступа к операциям бизнес-логики на базе Spring Security.  
Информацию об учётных записях пользователей необходимо сохранять в реляционную базу данных.  
Для аутентификации использовать HTTP Basic.

### Асинхронное выполнение задач и планировщик:

Реализовать асинхронное выполнение задач с распределением бизнес-логики между несколькими вычислительными узлами и выполнением периодических операций с использованием планировщика задач.

### Требования к асинхронной обработке:

- Асинхронное выполнение задач должно использовать модель доставки "подписка".
- В качестве провайдера сервиса асинхронного обмена сообщениями необходимо использовать сервис подписки на базе Apache Kafka + ZooKeeper.
- Для отправки сообщений необходимо использовать Kafka Producer API.
- Для получения сообщений необходимо использовать API KafkaConsumer (org.apache.kafka.clients.consumer.KafkaConsumer).

### Требования к распределённой обработке:

- Обработка сообщений должна осуществляться на двух независимых друг от друга узлах сервера приложений.

### Планировщик задач:

Реализовать утверждённые прецеденты с использованием планировщика задач Spring (@Scheduled).
