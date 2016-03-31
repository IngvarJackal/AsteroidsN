# AsteroidsN
It's [asteroids](https://en.wikipedia.org/wiki/Asteroids_%28video_game%29) with gravity



## Gameplay
Player should defend Earth from asteroids

## Concepts
* No physics for Moon -- moves by circular orbit.
* Earth and Moon affect ship and asteroids.
* Asteroids kill player
* Big asteroids split up when hit
* Player can refuel by orbiting over the Moon
* There is helper trajectory line

## How to build
```./gradlew desktop:dist``` will generate jar in desktop/lib
