[![GitHub](https://img.shields.io/github/license/lprakashv/expression-parser?style=flat-square)](LICENSE)
![Test Badge](https://img.shields.io/endpoint?url=https://gist.githubusercontent.com/lprakashv/d79f9f3049bf190b417943a2d3b21481/raw/expression-parser__heads_master.json)
![Coverage Badge](https://img.shields.io/endpoint?url=https://gist.githubusercontent.com/lprakashv/2f297a650e2c9fea0b3b817691fada74/raw/expression-parser__heads_master.json)

# expression-parser
Continuous REPL Parser for user defined expression (using [fastparse](http://www.lihaoyi.com/fastparse/))

#### Pre requisites:
* java - on Mac: `brew cask install adoptopenjdk8`
* sbt - on Mac: `brew install sbt`

#### Example expressions:
* `1.34 - 234.5 + 3443 ^ 2 + sqrt(69 - 5) * log(34)`
* `sqrt(3 ^ 2 + 4 ^ 2)`
* `sqrt(add(square(3), square(4)))`
* `add(12.0, 23, -1, 030, 343,23)`
* `quit` or `exit`

#### Run using:
`sbt run`

#### Create a standalone jar using:
`sbt assembly`

#### Preview
![demo](demo/expr-par-demo.gif)

#### TODOs:
- [ ] Add invalid arguments error for functions.
- [ ] Add function name auto-completion.
- [ ] Add expression color highlighting (multi-color).
- [ ] Add expression history and ANSI console edit support.
