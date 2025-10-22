package com.eszsz.prometheus_grafana_demo.config;

import com.eszsz.prometheus_grafana_demo.model.User;
import com.eszsz.prometheus_grafana_demo.repository.UserRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class UserBatchConfig {

    @Bean
    public FlatFileItemReader<User> reader() {
        return new FlatFileItemReaderBuilder<User>()
            .name("userItemReader")
            .resource(new ClassPathResource("users.csv"))
            .delimited()
            .names("id","name","email")
            .targetType(User.class)
            .linesToSkip(1) // Skip header line
            .build();
    }

    @Bean
    public ItemProcessor<User, User> processor() {
        return user -> {
            System.out.println("Processing user: " + user.getName());
            user.setName(user.getName().toUpperCase());
            return user;
        };
    }

    @Bean
    public RepositoryItemWriter<User> writer(UserRepository repository) {
        RepositoryItemWriter<User> writer = new RepositoryItemWriter<>();
        writer.setRepository(repository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("importUserJob", jobRepository)
            .start(step)
            .build();
    }

    @Bean
    public Step userProcessingStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                     ItemReader<User> reader, ItemProcessor<User, User> processor,
                     RepositoryItemWriter<User> writer) {
        return new StepBuilder("userProcessingStep", jobRepository)
            .<User, User>chunk(5, transactionManager)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build();
    }
}
