These codes are collected from the book of "Storm Blueprints", chapter 4.And modified to work with newest version of storm or kafka.

Running logback logging message generator.Create message and send them to kafka.

$ mvn exec:java -Dexec.mainClass=chapter4.logback.RogueApplication

Running storm topoloy
$ mvn exec:java -Dexec.mainClass=chapter4.LogAnalysisTopoloy 