# Bookmaker
Niniejszy projekt stanowi system do **zakładów bukmacherskich**. Polega on na umożliwieniu użytkownikom obstawianie dostępnych meczów. Jeśli kupon okaże sie wygrany, do salda konta uzytkownika dodawana jest wygrana kwota. Ponizej przedstawiam proces instalacji, użycia i zastosowanych narzędzi i technik.

## Instalacja aplikacji
W pierwszej kolejności należy skolonować repozytorium za pomoca poleceń:
```
git clone https://github.com/Siemoniere/Bookmaker.git
cd Bookmaker/Bukmacher
```
Następnie logujemy sie do konta na MariaDB i tworzymy nową bazę:
```
mysql -u <twoj_login> -p
```
Po wpisaniu hasła:
```
CREATE DATABASE <twoja_nazwa_bazy>
EXIT;
```
Następnie rozpakowujemy backup i przechodzimy do pliku config.properties:
```
mysql -u <twoj_login> -p <twoja_nazwa_bazy> < backup.sql
cd src/main/resources
```
I zmieniamy nasze dane w tymże pliku na nasze:
```
db.url=jdbc:mariadb://localhost:3306/<twoja_nazwa_bazy>?loggerLevel=OFF
db.adminuser=bukmacher
db.adminpassword=bukmacher
db.user=<twoj_login>
db.password=<twoje_haslo>
```
Następnie uruchamiamy projekt poleceniem:
```
mvn clean compile exec:java
```
## Użycie aplikacji
### Tworzenie konta i logowanie
Jeśli korzystasz z aplikacji po raz pierwszy, musisz założyć konto. Pamiętaj, że musisz mieć ukończone 18 lat. W przeciwnym wypadku należy się od razu zalogować.

### Saldo portfela
Wchodząc w saldo potfela możemy wpłacać i wypłacać środki, lub jedynie sprawdzać ich stan.

### Stawianie kuponów
W aplikacji możemy przeglądać dostępne mecze, wybrać jeden konkretny i obstawić wygrany w nim zespół, widząc aktualny kurs na każdą z opcji. Następnie wprowadzamy kwotę, jaką chcemy obstawić na wybrany zespół. W przypadku poprawnego typu, wraz z zakończeniem się meczu do naszego salda dopisywana jest postawiona kwota * nasz kurs *  0,88 (podatek od wygranej).

Ze względu na to, że niniejszy projekt stanowi symulację prawdziwego systemu do zakładów bukmacherskich, nie każdy mecz realnie sie odbywa. W związku z tym, po minięciu daty danego meczu, jest on automatycznie rozstrzygiwany losowo, biorąc pod uwagę prawdopodobienstwo danej opcji wynikające z wysokości kursu.

## Wykorzystane narzędzia i ich zastosowanie
Do stworzenia aplikacji użyłem następujących narzędzi:
-  Baza danych **MariaDB**
-  **Maven**

### MariaDB
Poniżej znajduje się mój projekt bazy danych:

https://github.com/Siemoniere/Bookmaker/blob/main/document.pdf

Niniejsza baza spełnia zasadę 3NF, zawiera dwa poziomy dostepu (admin i user) oraz zawiera szereg procedur i triggerów odpowiednio optymalizujących bazę danych, np. trigger usuwający każdy kupon zawierający dany mecz, który właśnie się skończył, w.w. procedura rozstrzygająca miniony właśnie mecz.

##### Admin
Jest to poziom niedostępny dla użytkowników aplikacji. Jako admin posiadam inny interfejs aplikacji i mogę tworzyć nowe mecze oraz zmieniać kursy istniejących. Dlatego w przypadku braku dostępnych meczów oznacza to, że przestałem je systematycznie dodawać i każdy z nich minął.

### Maven
Pisząc program w Javie skorzystałem z **bcrypt** do bezpiecznego szyfrowania haseł podczas tworzenia kont przez użytkowników. Ponadto skorzystałem z Prepared Statementów i własnego Scannera, który blokował jakiekolwiek dane wprowadzane przez usera, które zawierały niebezpieczne znaki. Dzięki temu unikamy **SQL Injection**.
