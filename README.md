# Multiplayer Checkers [![Build Status](https://travis-ci.org/AdamStelmaszczyk/Checkers.svg?branch=master)](https://travis-ci.org/AdamStelmaszczyk/Checkers)

Build (the same in [`.travis.yml`](https://github.com/AdamStelmaszczyk/Checkers/blob/master/.travis.yml)):

```
mkdir bin
javac -d bin $(find src -name *.java)
jar cfe checkers.jar Checkers -C src .
```

Run:

```
java -jar checkers.jar &
```

You should see:

<img src="http://i.imgur.com/Vrx1yhA.png"/>

Run it again to have two windows, click "Stwórz grę" (Create a game) in one of them.

Then go to the other window and click "Dołącz do gry" (Join a game). Dialog will appear asking you for server IP address - leave the default (127.0.0.1) and press OK.

A game should start:

<img src="http://i.imgur.com/gHESyRn.png"/>


