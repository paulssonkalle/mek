#!/bin/bash
echo 'mysecretkey' > keyfile
chmod 400 keyfile
sudo chown 999:999 keyfile