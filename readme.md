# ParticleLib
This is a paper plugin library for creating custom particle effects in Minecraft. The particles are rendered using 
Minecraft's display entities (text displays for 2d particles and item displays for 3d particles).  
  
I mainly made this library so I could create interesting particle effects for my own plugins, but I thought it would be
interesting to try fully completing and documenting a project of mine.   

# Installation
To install to your local repository, clone this repo somewhere and run the maven install goal on the ParticleLib
directory, or you could convert everything to gradle if you want.  
 
As for putting this in your projects, you get to plop this little thing into your pom.xml dependencies. Then you just
gotta throw the compiled version into your plugins folder in your server along with your plugin (unless you shade the dependencies)
```xml
<dependency>
    <groupId>me.pm7</groupId>
    <artifactId>particlelib</artifactId>
    <version>1.0</version>
    <scope>provided</scope>
</dependency>
```

The texture pack required for the cube/shadeless cube particles is `particleLibBase.zip` in the repo

# How it works  
If you don't want to read my waffling on about how the thingy works, I encourage you to look into the code in the
examples part of this repository. I've tried to make it mostly clean and self explanatory. However, if you like reading
long rambles describing a project...  
 
Attempting to sum it up in one vague sentence, ParticleEmitter objects store ParticleBuilders and use the 
ParticleBuilder to spawn Particles when some certain criteria is met. This is as far as I got before finding out I was 
really bad at explaining this in an overview-y sort of way, so I will instead opt to start from the bottom and work my 
way up.

## Basic Data
Particles have data that determines how they act across their lifetime. In this library, particle data is either
in the form of a constant value (basic boolean, double, etc.), a ValueRange<> (range between two values), a Gradient 
(list of keyframes to be interpolated between across a particle's life),
or a RangedGradient, (list of ranged keyframes to be randomly baked into a Gradient when a particle
spawns). There is also a Direction class to store yaw & pitch for direction values.
 
### Bit more gradient info
Keyframes contain a value and a position between 0.0 and 1.0, with 0.0 being the start of a particle's life, and 1.0
being the end. The value is any object, but GradientColor goes with Keyframe\<Color>, GradientDouble with 
Keyframe\<Double>, and so on. Gradients also have an EasingMode enum, but it applies to the entire gradient's range
rather than the individual keyframes so you'll probably just use EasingMode.LINEAR most of the time. If I were smarter
I would have made each keyframe have an EasingMode, but I didn't  
RangedKeyframes contain a position between 0.0 and 1.0, but contain two values of the same type. When a particle is spawned with a 
RangedGradient, it "bakes" the RangedKeyframes into normal Keyframes by randomly picking between the
two values.

## Particles
Particles are spawned (built) from ParticleBuilder objects. There are a few different kinds of ParticleBuilders, which
have specific purposes. If you want to know what this library is capable of without diving into its documentation, this 
is an exhaustive list of every controllable aspect of particles

All ParticleBuilders have the following data:
- life ticks - how long the particle lives for once emitted (int or ValueRange\<Integer>)
- ticks per calculation - lag preventing measure, more on that later (integer)
- spawn offset - where the particle spawns relative to its emitter (Vector or ValueRange\<Vector>)
- (initial) movement direction - The direction in which the particle moves at the start of its life (Direction or 
ValueRange\<Vector>)
- scale - How large the particle is (Vector, GradientVector, RangedGradientVector)
- rotation over velocity - how much the particle should rotate on all available axes depending on its velocity in
degrees per meter per second (Double, ValueRange\<Double>)
- gravity - How the velocity of the particle should change over time (more on this in a bit)

All 2D ParticleBuilders (ParticleBuilderSquare, ParticleBuilderCustomText) have this extra data:
- initial roll - How far (in degrees) the particle should be rotated on the roll axis when it spawns (double, 
ValueRange\<Double>)
- roll speed - how much (in degrees per second) the particle should rotate over time (double, GradientDouble, 
RangedGradientDouble)
- color - What color the text display should render as (Color, GradientColor, RangedGradientColor)
- shaded - if the particle should be affected by its surrounding light or not (boolean)

ParticleBuilderSquare has no additional data.
ParticleBuilderCustomText has the following extra data:
- text - What text to render on the display (Component)

All 3D ParticleBuilders (ParticleBuilderBlock, ParticleBuilderCube, ParticleBuilderCustomItem) have this extra data
- initial rotation - How far (in degrees) the particle should be rotated when it spawns (Vector, ValueRange\<Vector>)
- velocity overrides rotation - if the particle should always face in the direction its moving (boolean)
- rotation speed - how much (in degrees per second) the particle should rotate over time (Vector, GradientVector,
RangedGradientVector)

ParticleBuilderBlock has the following extra data:
- block data - The block data to use when rendering the block (BlockData)

ParticleBuilderCube has the following extra data:
- color - The color to render the cube as (Color, GradientColor, RangedGradientColor)
- shaded - if the particle should be affected by its surrounding light or not (boolean)    

ParticleBuilderCustomItem has the following extra data:
- color - The color to render the cube as (Color, GradientColor, RangedGradientColor) **[note, this only gets used if
you set up your item to use potion contents colors in a resource pack]**
- item - the item to render the particle as (ItemStack)

## Gravity
The movement of particles is controlled by a Gravity. There are four types of gravities, each of which is admittedly
very simple in its execution. (I haven't ever made a gravity simulation before, there's definitely a couple mistakes)  
anyway, I'll provide a brief description of each gravity, leaving the detailed explanation in the javadocs
- GravityNone - Moves particles at a speed without ever changing its movement direction
- GravityDirection - Probably the most important gravity. It handles gravity in a specified vector direction
- GravityLocation - Gonna be honest I kinda accidentally made this one. It simulates gravity around a point relative
to the emitter's location. Good if you want planetary motion or something I guess
- GravityAxis - Made specifically to recreate the fire effect from Cymaera's video, this gravity pushes particles
both towards and along an axis

## Particle Emitters
I mentioned particles were spawned by emitters, so I should probably talk about those. ParticleEmitters do in fact spawn
particles. They are represented in physical space by an empty BlockDisplay entity. This allows emitters to be teleported
easily, mounted to other entities, and all other wacky stuff. All emitters are paused when they are spawned, so remember to run the start
method in your code.
  
I made three types of emitters:
- ParticleEmitterConstant - Emits particles at a constant, specified rate
- ParticleEmitterBurst - When played, emits a number of particles before removing itself
- ParticleEmitterMovement - Emits particles based on how far the emitter has moved since the last tick

# Notes  
- ParticleBuilderCube uses my custom resource pack. The rest of the ParticleBuilders do not require a resource pack.
- All data classes can be stored and loaded from a standard paper config.
- Currently, this plugin removes all emitters and particles when the server closes. This is because I do not trust my
code enough to make a persistent emitter. I think it should be pretty easy to use the aforementioned config system to
save and load data about particle systems to keep them persistent.

# Lag
**so... each particle is an entity constantly being teleported and updated? Isn't this whole thing super laggy?**  
  
On the client? Not at all. Every client I've tested has been able to handle multiple thousand particles on screen at
once. Maybe your friend using super ultra deluxe shaders might finally drop below 144 fps, but as far as I can tell
this works pretty smoothly on the client.  
  
The server is another story. Teleportations are computationally expensive in minecraft, which is why most of
my lag preventing measures focus on that. Every emitter can have its own maximum number of particles set and a view 
distance at which the emitter will stop spawning particles, but ParticleData also can have the "ticks per calculation" 
set. By default, this number is 1, and every particle is teleported and updated every game tick, but by setting the 
ticks per calculation to numbers greater than 1, the particle will only be updated by the plugin every x amount of 
ticks, greatly reducing the number of teleportations per tick.
