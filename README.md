<a href="https://gitter.im/pouyacode/alexa-monitor?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge"><img src="https://badges.gitter.im/pouyacode/alexa-monitor.svg" /></a>
[Code of Conduct](https://github.com/pouyacode/alexa-monitor/blob/master/CODE_OF_CONDUCT.md)
[Documents](https://alexa-monitor.pouyacode.net)

---
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

Generate docs (follow instructions from [Marginalia](https://github.com/gdeer81/lein-marginalia)):
```
lein marg -f index.html
```


### Local Sample data
There's a sample html file in `resources` directory, all links are changed to point at `localhost` so it's basically an ugly copy of Alexa`s Mini Siteinfo. You can run a simple webserver on port 8080 to serve this file, and use that for test (instead of submitting actual requests to alexa.com)
```
cd resources
python3 -m http.server 8080
```


## Options

No options yet.


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
