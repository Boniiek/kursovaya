# RESTful сервис myDisk

## Развертывание в среде linux
Т.к. запускать мы будет конечный jar, нам потребуется JRE,
помимо этого для сборки мы будем мспользовать Maven, который требует
JDK, поэтому установим его с помощью кломанды:
```
sudo apt install openjdk-11-jdk
```
После чего требуется добавить переменную окружения
JAVA_HOME
```
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk
```
Помимо этого нужно развернуть БД, лично я использовал postgres, и файл properties
также был сконфигурирован под неё:
```
sudo apt -y install postgresql
```
После чего нужно создать БД, которую я назвал myDisk. Затем создаем таблицу element:
```
CREATE TABLE element(
id varchar UNIQUE NOT NULL PRIMARY KEY,
url varchar,
parent_id varchar,
date timestamp,
size bigint CHECK ( size >= 0),
type varchar);
```
Для развертывания сервиса потребуется Maven, который можно установить с помощью команды:
```
sudo apt install maven
```
После этого клонируем репозиторий с гитлаба через ssh (ключи надо предварительно сгенерировать)
и в корне проекта запустить команду для сборки сервиса:
```
mvn clean install
```



Теперь в папке target появится исполняемый .jar файл, чтобы linux автоматически 
запускал наш сервис при запуске, нам нужно создать сервис в /etc/systemd/system/
с именем myDisk.service, и ввести следующий код:

```
[Service]
ExecStart=sudo java -jar /home/ubuntu/myDisk/myDisk/target/myDisk-0.0.1-SNAPSHOT.jar
User=ubuntu

[Install]
WantedBy=multi-user.target
```

Далее нужно поставить сервис на автозапуск:
```
sudo systemctl enable myDisk
```

После чего наш сервис будет запускаться при запуске linux

## Описание приложения

Сервис работает на порту 80, а также прослушивает все сетевые интерфейсы (0.0.0.0)

Были реализованы все 5 REST методов, входящие json'ы проходят соответствующую валидацию
(Это было для меня самой сложной задачей, потратил почему-то больше половины всего времени :(

).

Работа с БД происходит посредством спецификации JPA, которую реализует Hibernate.
