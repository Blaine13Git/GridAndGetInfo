echo =========start grid hub=============
set url=http://127.0.0.1:4444/grid/console
set jar=Jars\selenium-server-standalone-2.53.0.jar
java -jar %jar% -role hub %url%
