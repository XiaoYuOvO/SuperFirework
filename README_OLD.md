<h2 style="text-align: left;">What does this mod do?</h2>

vanilla Minecraft's fireworks are not big enough and them have not many styles.So this mod adds some amazing fireworks into minecraft

You can find all the fireworks in the misc creative table 

## Fireworks Examples

#### Super Firework:

Large ball:
![Large ball shape](https://i.imgur.com/FYITnrg.png)
Trible sphere:
![](https://i.imgur.com/kWntqNm.png)
#### Clone Firework(Launch four same fireworks at four corners when explode)

![](https://i.imgur.com/ijaWsoE.png)

<h2 style="text-align: left;">Other Information</h2>

Clone firework can be spawned by set Clone:1 tag into superfirework:super\_firework entity,for example: /summon superfirework:super\_firework ~ ~ ~ {Clone:1}

You can set the size of the firework particle amount and speed of the firework particle by giving a NBT tag to the super firework entity,for example:

/summon superfirework:super\_firework ~ ~ ~ {FireworksItem:{id:fireworks,Count:1,tag:{Fireworks:{Speed:5,Explosions:\[{Size:10,Type:0,Colors:\[I;16711680\],FadeColors:\[I;16711680\]}\]}}}}

This command will spawn a firework and its particle's size and speed are 10 and 5,the color and fade color of particles are red

If you only spawn a simple firework without giving it NBT,its NBT will be set randomly

If you only set some arguments of the firework,other arguments will be set randomly,for example:

/summon superfirework:super\_firework ~ ~ ~ {FireworksItem:{id:fireworks,Count:1,tag:{Fireworks:{type:1,Explosions:\[{Colors:\[I;16711680\]}\]}}}}

This command will spawn a firework and its color is red,its speed and fade color will be generated randomly(You can put tag NoFade:1 tag inside the Explosions tag and make it hasn't fade color)

#### Firework Type Mapping

1 -> ball

2 -> star shape

3 -> creeper shape

4 -> burst

5 -> trible ball

6 -> irregular ball

default(type>6 or type<1) -> ball

## Issue and suggestions

Please sent your suggestions and issue to [Here](https://github.com/XiaoYuOvO/SuperFirework/issues/new)