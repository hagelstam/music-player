# Music Player

## Uppgift 1 beskrivning

Till uppgift 1 bidrog Maximilian Hagelstam och Johnny Do. Vi täknte på vilken funktionalitet som kommer att behövas och
fösökte hålla lösningen så simpel som möjligt. Album klassen är den viktigaste. Den är vad programmet är baserat på.
Svåraste delen var att klura ut tillsättning och radering av sub albumar och ljudklipp skall påverka andra subalbum och
förälderalbum. Vi kommer troligen att måstaändra på vår lösning då vi börjar jobba på funktionaliteten och märker fel i
vår nuvarande lösning.

## Uppgift 2 beskrivning

Till uppgift 2 bidrog Maximilian Hagelstam och Johnny Do. Det största arbetet var att integrera den givna koden i projektet och få JavaFX att fungera med Maven. När vi fick det att fungera var det relativt enkelt att implementera den funktionalitet som saknades, tack vare TODO kommentarerna.

## Uppgift 3 beskrivning

Till uppgift 3 bidrog Maximilian Hagelstam och Johnny Do. Vi började med att skapa en ny AlbumObserver interface och en AlbumWindow klass som implementerar den. Sedan gjorde vi ändringar Album klassen, så att den skulle använda sig av observern. Vi behövde också göra en liten ändring till hur ljudklipp spelas upp pga en bug som gjorde att ljudklipp inte spelades i nya fönster.
