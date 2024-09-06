//package net.duchung.quora.common.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
//
//@Configuration
//@EnableElasticsearchRepositories
//        (basePackages = "net.duchung.quora.repository.elastic")
//
//public class ElasticSearchConfig extends ElasticsearchConfiguration {
//    @Override
//    @Bean
//    public ClientConfiguration clientConfiguration() {
//        return ClientConfiguration.builder()
//                .connectedTo("localhost:9200")
//                .build();
//    }
//}
