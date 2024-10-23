# AdventQuest

# Opis projekta
Ovaj projekt je reultat timskog rada u sklopu projeknog zadatka kolegija [Programsko inženjerstvo](https://www.fer.unizg.hr/predmet/proinz) na Fakultetu elektrotehnike i računarstva Sveučilišta u Zagrebu. 

Cilj ovog projekta je izraditi AdventQuest, web aplikaciju za lakšu organizaciju društvenih događanja u periodu božićnih praznika. 

Projektom namjeravamo ubrzati i uvelike olakšati kreiranje blagdanskih aktivnosti i prijavljivanje na njih kako bismo korisnike oslobodili nezanimljivih zadataka poput raspitivanja o terminima događanja te oglašavanja aktivnosti i prijava na njih putem telefonskih poziva, emaila, poruka, društvenih mreža i drugih nespecijaliziranih kanala komunikacije. Aplikacija će uključivati AI agenta za pomoć pri postavljanju aktivnosti u raspored.

Nadamo se da ćemo korisnicima uštedjeti energiju i vrijeme koje umjesto u dosadne administrativne zadatke mogu uložiti u više zabavljanja tijekom blagdanske sezone.

Sudjelujući na projektu želimo naučiti koristiti nove razvojne okvire i paradigme navedene pod [tehnologije](#tehnologije), alate za planiranje arhitekture sustava, upravljanje verzijama izvornog koda i koordinaciju timskog rada (alati za izradu UML dijagrama, git, GitHub). Usput se upoznajemo s ozbiljno vođenim i strukturiranim timskim radom kakav se inače očekuje u inženjerstvu.

# Funkcijski zahtjevi
- Korisnici (sudionici, „božićni predsjednici“ koji organiziraju događanja i administrator sustava) se registriraju. Kasnije se prije početka rada moraju prijaviti u sustav. Sustav koristi OAuth 2.0 standard za autentikaciju.
- Korisnici imaju prikaz odbrojavanja vremena do Božića u vremenskim jedinicama od dana do sekunda. Odbrojavanje se ažurira u stvarnom vremenu.
- **kreiranje korisničkih grupa**: Božićni predsjednik kreira grupu i bira sudionike grupe među registriranim korisnicima
- Inicijalni prikaz grupe nudi prazan upravljački panel i otvoreni chat za sve sudionike
- **dodavanje aktivnosti**: moguća su tri načina dodavanja grupne aktivnosti
  - Božićni predsjednik ručno doda aktivnosti
  - Božićni predsjednik odabere iz već ponuđenih božićnih aktivnosti one koje smatra da bi bile zanimljive za njegovu grupu sudionika
  - AI agent automatski doda aktivnost na temelju poruka u chatu (definirati jasnije postupak kada dođemo do implementacije AI agenta).
 
Dodane aktivnosti vidljive su na upravljačkom panelu.
 - Nakon završetka aktivnosti, sudionici po želji daju povratnu informaciju (like ili dislike i komentar).
 - Podaci o sudjelovanju sudionika grupe u chatu, aktivnostima ili povratne informacije koje sudionici ostave za pojedinu aktivnost mogu biti analizirani od strane AI agenta te svim sudionicima mogu biti predložene promjene ili poboljšanja u organizaciji budućih aktivnosti.
 - Administrator ima pristup svim dijelovima aplikacije, uključujući kreiranje i brisanje korisničkih računa, upravljanje aktivnostima, grupama i obavijestima.

# Tehnologije
strana poslužitelja: Java, Spring Boot  
strana klijenta: React  
relacijska baza podataka  
API za AI agenta: (odlučiti)
# Instalacija
# Članovi tima 
Nikola Colnar  (nikola.colnar@fer.unizg.hr)  
Roko Vrdoljak  (roko.vrdoljak@fer.unizg.hr)  
Marko Grgurić  (marko.grguric2@fer.unizg.hr)  
Karlo Matanić  (karlo.matanic@fer.unizg.hr)  
Tomislav Pap   (tomislav.pap@fer.unizg.hr)  
Antonio Poleto (antonio.poleto@fer.unizg.hr)  
Lovro Vuletić  (lovro.vuletic@fer.unizg.hr)

# Kontribucije
>Pravila ovise o organizaciji tima i su često izdvojena u CONTRIBUTING.md



# 📝 Kodeks ponašanja [![Contributor Covenant](https://img.shields.io/badge/Contributor%20Covenant-2.1-4baaaa.svg)](CODE_OF_CONDUCT.md)
Kao studenti sigurno ste upoznati s minimumom prihvatljivog ponašanja definiran u [KODEKS PONAŠANJA STUDENATA FAKULTETA ELEKTROTEHNIKE I RAČUNARSTVA SVEUČILIŠTA U ZAGREBU](https://www.fer.hr/_download/repository/Kodeks_ponasanja_studenata_FER-a_procisceni_tekst_2016%5B1%5D.pdf), te dodatnim naputcima za timski rad na predmetu [Programsko inženjerstvo](https://wwww.fer.hr).
Očekujemo da ćete poštovati [etički kodeks IEEE-a](https://www.ieee.org/about/corporate/governance/p7-8.html) koji ima važnu obrazovnu funkciju sa svrhom postavljanja najviših standarda integriteta, odgovornog ponašanja i etičkog ponašanja u profesionalnim aktivnosti. Time profesionalna zajednica programskih inženjera definira opća načela koja definiranju  moralni karakter, donošenje važnih poslovnih odluka i uspostavljanje jasnih moralnih očekivanja za sve pripadnike zajenice.

Kodeks ponašanja skup je provedivih pravila koja služe za jasnu komunikaciju očekivanja i zahtjeva za rad zajednice/tima. Njime se jasno definiraju obaveze, prava, neprihvatljiva ponašanja te  odgovarajuće posljedice (za razliku od etičkog kodeksa). U ovom repozitoriju dan je jedan od široko prihvačenih kodeks ponašanja za rad u zajednici otvorenog koda.

# 📝 Licenca
Važeča (1)
[![CC BY-NC-SA 4.0][cc-by-nc-sa-shield]][cc-by-nc-sa]

Ovaj repozitorij sadrži otvoreni obrazovni sadržaji (eng. Open Educational Resources)  i licenciran je prema pravilima Creative Commons licencije koja omogućava da preuzmete djelo, podijelite ga s drugima uz 
uvjet da navođenja autora, ne upotrebljavate ga u komercijalne svrhe te dijelite pod istim uvjetima [Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License HR][cc-by-nc-sa].
>
> ### Napomena:
>
> Svi paketi distribuiraju se pod vlastitim licencama.
> Svi upotrijebleni materijali  (slike, modeli, animacije, ...) distribuiraju se pod vlastitim licencama.

[![CC BY-NC-SA 4.0][cc-by-nc-sa-image]][cc-by-nc-sa]

[cc-by-nc-sa]: https://creativecommons.org/licenses/by-nc/4.0/deed.hr 
[cc-by-nc-sa-image]: https://licensebuttons.net/l/by-nc-sa/4.0/88x31.png
[cc-by-nc-sa-shield]: https://img.shields.io/badge/License-CC%20BY--NC--SA%204.0-lightgrey.svg

Orginal [![cc0-1.0][cc0-1.0-shield]][cc0-1.0]
>
>COPYING: All the content within this repository is dedicated to the public domain under the CC0 1.0 Universal (CC0 1.0) Public Domain Dedication.
>
[![CC0-1.0][cc0-1.0-image]][cc0-1.0]

[cc0-1.0]: https://creativecommons.org/licenses/by/1.0/deed.en
[cc0-1.0-image]: https://licensebuttons.net/l/by/1.0/88x31.png
[cc0-1.0-shield]: https://img.shields.io/badge/License-CC0--1.0-lightgrey.svg

### Reference na licenciranje repozitorija
