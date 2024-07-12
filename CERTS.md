## HowTo: Create a CA and a Server Certificate

**Note:** _I used the password `changeIt` for this sample_
### Extension files
**ca.ext**
```
[req]
default_bits = 4096
distinguished_name = req_distinguished_name
string_mask         = utf8only
default_md          = sha3-384
x509_extensions     = v3_ca

[ v3_ca ]
basicConstraints = critical,CA:TRUE
subjectKeyIdentifier = hash
keyUsage = critical, keyCertSign
authorityKeyIdentifier = keyid:always,issuer

[req_distinguished_name]
C = ab
O = xyz
OU = company
CN = rootCA
```

**cert.ext**
```
[req]
req_extensions = v3_req

[ v3_req ]
basicConstraints = CA:FALSE
nsCertType = server
nsComment = "OpenSSL Generated Server Certificate"
subjectKeyIdentifier = hash
authorityKeyIdentifier = keyid,issuer:always
keyUsage = critical, digitalSignature, nonRepudiation, keyEncipherment, dataEncipherment
extendedKeyUsage = serverAuth
subjectAltName = @alt_names

[alt_names]
DNS.1 = www.devstuff.org
```


### Create CA Key
```
openssl genrsa -aes-256-cbc -out myCA.key 4096
```

### Create CA pem
**Note: A sample ca.ext is listed above**
```
openssl req -x509 -new -nodes -config ca.ext -key myCA.key -sha3-384 -days 3825 -out myCA.pem
```

### Create Key
```
openssl genrsa -aes-256-cbc -out devcert.key 4096
```

### Create CSR for CA
```
openssl req -new -key devcert.key -out devcert.csr
```

### Create pem from CSR
**Note: A sample cert.ext is listed above**
```
openssl x509 -req -in devcert.csr -CA myCA.pem -CAkey myCA.key -out devcert.pem \
-days 3850 -sha3-384 -CAcreateserial -extensions v3_req -extfile cert.ext
```

## Optional
### Create keystore
```
openssl pkcs12 -in devcert.pem -inkey devcert.key -passin pass:changeIt -certfile myCA.pem \
-export -out keystore.p12 -passout pass:changeIt -name server
```

### Create truststore
```
openssl pkcs12 -in myCA.pem -nokeys -export -out truststore.p12 -passout pass:changeIt -name CA
```
or
```
keytool -importcert -storetype PKCS12 -keystore truststore.p12 \
-storepass changeIt -alias ca -file myCA.pem -noprompt
```

## HowTo View your Certificates
```
openssl x509 -in myCA.pem -text

openssl x509 -in devcert.pem -text

openssl x509 -in keystore.p12 -text

openssl x509 -in truststore.p12 -text
```
