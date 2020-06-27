# Kasyno
Projekt na studia. W Java zaprojektowanie symulacji z wieloma wątkami komunikującymi ze sobą z wizualizacją w Swing

Wymagania projektu:
- w języku Java wykorzystując technologię gui Swing lub JavaFx stworzyć symulację, 
np. restauracji gdzie wątek klienta i kelnera mają ze sobą interakcję, kelner podchodzi jak klient zasiadzie do stołu i podaje jedzenie
 - powinien zawierać "wąskie gardło", przejście z którego jeden wątek może korzystać takie że tylko jedna osoba może jednocześnie przechodzić, np. schody na drugie piętro kawiarni.

Postanowiłem zrobić Salę gier (które zmieniło się w kasyno z jednorękim bandytą), wąskim gardłem będą schody do piętra z kabinami. 

Na pierwszym piętrze (dolna sala na rysunku) jest kasa z pracownikiem wydającym żetony, a na drugim kabiny do gry, które drugi pracownik szykuje do użytkowania (pracownicy są koloru fioletowego - magenta). 

Po uruchomieniu 10 klientów zostaje wywołanych reprezentowanych przez szare koła, którzy podchodzą do kasy i pokolei pobierają żetony od kasjera (po 5 na głowę, a pracownik może trzymać tylko 10 zanim nie wyrusza do magazynu po kolejne). Liczba żetonów jest wyświetlana na każdym kliencie i kasjerze, po otrzymaniu żetonu klient staje się żółty i rusza do schodów przechodząc przez nie bądź czekając w kolejce aż zostanie zwolniony dostęp.

Po wejściu do pokoju z machinami gier klient wybiera losową kabinę, która jest nieużywana i aktywna następnie je zajmuje i rozpoczyna grę. Losowo traci lub zyskuje żetony, jeśli dojdzie do 0 to zmienia kolor na czerwony, jeśli wygra to zmienia na zielony - po zmianie stanu przerywa grę i zwalnia kabinę (ta staje się nieaktywna, co pracownik zaraz uaktywni) po czym wraca do schodów (zajmuje miejsce jeśli ktoś jest w kolejce z dowolnej strony) i wychodzi z kasyna. 

Jak jakiś klient opuści teren to automatycznie powstaje nowy klient na jego miejsce i powtarza proces. Projekt miał zawierać stół do pokera oraz klient po przegraniu miał wrócić do kasy i grać aż wygra, ale nie zdążyłem tego zaimplementować.

Klasy, pliki projektu:
- Main.java - wywołuje JFrame Okno
- Okno.java - rozmiar okna i wywołuje zawartość JPanel Kasyno
- Kasyno.java - zawartość okna, deklaruje listę pracowników, klientów, swingworkerów dla każdego z nich, klasę kolejkę i timer rysujący obecny stan
- Obiekt.java - klasa abstrakcyjna, zawierające położenie i rozmiar (Klient.java, Pracownik.java i MaszynyDoGry.java je rozszerza)
- Kolejki.java - zawiera semafory i tablice boolean, które korzystają wszystkie swingworkery - tablice są dla kolejek do kasy, do schodów od dołu i od góry (oraz dodatkowy dla schodów, aby każdy przechodził w kolejności od zajęcia miejsca)
- SwingWorkerPracownik/Klient.java - wątki pracowników i klientów - każdy ma dostęp do tego samego kolejki.java

Projekt tworzony całymi dniami 22 i 23 czerwca 2020 i pokazany do prezentacji 27 czerwca
