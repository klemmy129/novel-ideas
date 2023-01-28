## HowTo: Create a CA and a Server Certificate

**Note:** _I used the password `changeIt` for this sample_
### Create CA Key
```
openssl genrsa -des3 -out myCA.key 2048
```

### Create CA pem
```
openssl req -x509 -new -nodes -key myCA.key -sha384 -days 3825 -out myCA.pem
```

### Create Key
```
openssl genrsa -out devcert.key 2048
```

### Create CSR for CA
```
openssl req -new -key devcert.key -out devcert.csr
```

### Create pem from CSR
**Note: A sample cert.ext is listed below**
```
openssl x509 -req -in devcert.csr -CA myCA.pem -CAkey myCA.key -out devcert.crt \
-days 3850 -sha384 -CAcreateserial -extfile cert.ext
```

**cert.ext**
```
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
keyUsage = digitalSignature, nonRepudiation, keyEncipherment, dataEncipherment
subjectAltName = @alt_names

[alt_names]
DNS.1 = servername.devstuff.org
```


### Create keystore
```
openssl pkcs12 -in devcert.crt -inkey devcert.key -passin pass:changeIt -certfile myCA.pem \
-export -out keystore.p12 -passout pass:changeIt -name devstuff
```

### Create truststore
```
keytool -importcert -storetype PKCS12 -keystore truststore.p12 \
-storepass changeIt -alias ca -file myCA.pem -noprompt
```

## HowTo View your Certificates
```
keytool -list -storetype PKCS12 -keystore truststore.p12 -storepass changeIt

keytool -list -storetype PKCS12 -keystore keystore.p12 -storepass changeIt
```
