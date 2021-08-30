git pull origin main &&
mvn clean compile assembly:single &&
mv "target/navi-1.0-with-dependencies.jar" "."
