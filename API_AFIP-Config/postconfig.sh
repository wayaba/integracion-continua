#!/bin/bash
# © Copyright SOA Team 2019.
#
# Todos los derechos reservados al equipo de SOA Yeah
# este archivo permite setear las configuraciones del broker
# integration node : MYNODE
# execution group : MYSERVICE


# --------------------------------NO EDITABLE INICIO-------------------------------------------------
# primero valido que el Broker ya esta running 
# loopeo hasta que aparezca el servicio running o pasen 30 segundos

printf "\n ------------------------------ESPERO QUE BROKER Y EG ESTE STARTED---------------"

runningtime=$(date +%s) #start time of the loop
endtime=$[$runningtime+60]
flagtimeout=1

printf "\nEspero hasta que el servicio este encendido o que pasen mas de 60s....\n"
while  [ `date +%s` -lt $endtime ] ; do
        if mqsilist MYNODE -e MYSERVER | grep "'MYSERVER' is running" > /dev/null
        then 
            echo "El servicio del broker ya esta corriendo"
            flagtimeout=0
            break
        fi
done

if [ $flagtimeout -eq 1 ]
then 
    printf "\nEl check del broker running salio por timeout\n" 
    exit 1
fi
# --------------------------------NO EDITABLE FIN-------------------------------------------------

# --------------------------------EDITABLE INICIO-------------------------------------------------
printf "\n ------------------------------CONFIGURACION EDITABLE----------------------------"

echo "----------------------------------------"
printf "\nAgrego keystore\n"
mqsichangeproperties MYNODE -o BrokerRegistry -n  brokerKeystoreFile -v /tmp/artifacts/Keystore.jks

CLAVE=`/tmp/scripts/getproperty.sh Keystore`
mqsisetdbparms MYNODE -n brokerKeystore::password -u ignore -p $CLAVE

echo "----------------------------------------"
printf "\nAgrego trustore\n"
mqsichangeproperties MYNODE -o BrokerRegistry -n brokerTruststoreFile -v /tmp/artifacts/Trustore.jks

CLAVE=`/tmp/scripts/getproperty.sh Trustore`
mqsisetdbparms MYNODE -n brokerTruststors::password -u ignore -p $CLAVE

# --------------------------------EDITABLE FIN-------------------------------------------------

# --------------------------------NO EDITABLE INICIO-------------------------------------------------
printf "\n ------------------------------REINICIO BROKER---------------"
printf "\nStoppeo el broker\n"

mqsistop MYNODE -i

# verifico que esta stoppeado totalmente el broker antes de volverlo a levantar
runningtime=$(date +%s) #start time of the loop
endtime=$[$runningtime+120]
flagtimeout=1

echo "----------------------------------------"
printf "\nEspero hasta que el servicio este apagado o que pasen mas de 120s....\n"
while  [ `date +%s` -lt $endtime ] ; do
        
        if ! ps -ef | grep -v grep | grep "bipservice" > /dev/null
        then 
            printf "\nEl servicio del broker ya esta apagado\n"
            flagtimeout=0
            break
        fi
done

if [ $flagtimeout -eq 1 ]
then 
    printf "\nEl broker aun no se apago y termino el while....\n" 
    exit 1
fi

echo "----------------------------------------"
printf "\nStarteo el broker\n"
mqsistart MYNODE &> /dev/null
printf "\nFin configuracion\n"
# --------------------------------NO EDITABLE FIN-------------------------------------------------