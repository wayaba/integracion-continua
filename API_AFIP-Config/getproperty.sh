TEXTO=$(/usr/bin/awk -F:= -v key="$1.texto" '$1==key{print $2}' /tmp/scripts/env.properties)
CLAVE=$(/usr/bin/awk -F:= -v key="$1.clave" '$1==key{print $2}' /tmp/scripts/env.properties)
/usr/bin/java -jar /tmp/artifacts/Encriptator-1.0.jar D $TEXTO $CLAVE

exit 0