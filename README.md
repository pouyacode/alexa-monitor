# alexa-monitor

Simple web crawler to collect [alexa](alexa.com) rank of a website and store in database.

## Installation

Install dependencies:
```
lein deps
```

## Usage

Run:
```
lein run
```


```
lein uberjar
```

And run the Jar file:
```
java -jar target/uberjar/alexa-monitor-0.1.0-SNAPSHOT-standalone.jar
```

Create GraalVM native-image:
```
lein native-image
```


## Options

No option yet.


## License

Copyright © 2021 Pouya Abbassi

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
