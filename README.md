**Support this work**
<!-- BADGES/ -->
<span class="badge-paypal">
<a href="https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&amp;hosted_button_id=MA847TR65D4N2" title="Donate to this project using PayPal">
<img src="https://img.shields.io/badge/paypal-donate-yellow.svg" alt="PayPal Donate"/>
</a></span>
<span class="badge-flattr">
<a href="https://flattr.com/submit/auto?fid=o6ok7n&url=https%3A%2F%2Fgithub.com%2Floxal" title="Donate to this project using Flattr">
<img src="https://img.shields.io/badge/flattr-donate-yellow.svg" alt="Flattr Donate" />
</a></span>
<span class="badge-gratipay"><a href="https://gratipay.com/~loxal" title="Donate weekly to this project using Gratipay">
<img src="https://img.shields.io/badge/gratipay-donate-yellow.svg" alt="Gratipay Donate" />
</a></span>
<!-- /BADGES -->

[Support this work with crypto-currencies like BitCoin, Ethereum, Ardor, and Komodo!](http://me.loxal.net/coin-support.html)

Quiz Service
=

# Pre-requisite / Configuration

Add a [configuration profile](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config-profile-specific-properties) 
to the `config` folder inside this project.

# API Reference

    http://localhost:8200/swagger-ui.html

# Initialization

## Couchbase

    docker rm -f couchbase
    docker run -d --name couchbase \
        -p 8091-8094:8091-8094 -p 11210:11210 \
        -v ~/srv/couchbase:/opt/couchbase/var/lib/couchbase \
        couchbase:community

    CREATE PRIMARY INDEX `#primary` ON `quizzer` 
    
## Cassandra

    docker rm -f cassandra
    docker run -it -d --name cassandra \
        -p 9042:9042 \
        -v ~/srv/cassandra:/var/lib/cassandra \
        cassandra:3

    CREATE KEYSPACE quizzer WITH REPLICATION = {'class':'SimpleStrategy', 'replication_factor' : 1};

# Run 

    ./run.sh
    open http://localhost:8200
    
# Test

    ./test.sh

`SPRING_CONFIG_NAME=application,local` environment property is required to add a specific configuration’s properties.    

# Benchmark / Performance Test

    ./gradlew clean jmh

# Release

    ./release.sh