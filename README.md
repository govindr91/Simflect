# Simflect

[![](https://jitpack.io/v/Lighture/simflect.svg)](https://jitpack.io/#Lighture/simflect)

Library that check if telephone is single SIM or dual SIM on Android.
Sim info:
* manufacturer
* model
* version code
* is dual SIM
* SIM details
  * is SIM ready
  * ID
  * operator name
  * MCC
  * MNC

## Code example:
```
SimManager.getPhoneDetails(context).blockingGet()
```
