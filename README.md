# Simflect

[![](https://jitpack.io/v/Lighture/simflect.svg)](https://jitpack.io/#Lighture/simflect)

Simflect project helps developers to identify if a device is single sim or multi sim. It also gathers some information related to the sim such as MNC code, MCC code, IMEI numbers, etc.

##Code example:
```
SimManager.getPhoneDetails(context).blockingGet()
```