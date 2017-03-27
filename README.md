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

# Initialization

## Couchbase

    CREATE PRIMARY INDEX `#primary` ON `quizzer` USING GSI
    
## Cassandra

    create keyspace quizzer with replication = {'class':'SimpleStrategy', 'replication_factor' : 2};

# Run 

    ./gradlew bootRun -D spring.config.name=prod,local,config
    open http://localhost:8200
    
# Test

    ./gradlew clean test

`SPRING_CONFIG_NAME=prod,local` is required to add a specific configuration’s properties.    

# Benchmark / Performance Test

    ./gradlew clean jmh

# Release

    ./release.sh