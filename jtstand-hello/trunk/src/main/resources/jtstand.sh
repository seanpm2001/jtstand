cd `dirname $0`
java -Xmx256m -Xms256m -Xmn64m -Dorg.jboss.logging.provider=log4j -Dsvnkit.wc.17.enabled=true -jar ${project.artifactId}-${project.version}.jar