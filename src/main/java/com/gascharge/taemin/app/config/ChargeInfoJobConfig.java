package com.gascharge.taemin.app.config;

import com.gascharge.taemin.app.api.ChargeApi;
import com.gascharge.taemin.app.api.ChargeApiDto;
import com.gascharge.taemin.domain.entity.charge.Charge;
import com.gascharge.taemin.domain.enums.charge.ChargePlaceMembership;
import com.gascharge.taemin.domain.repository.charge.ChargeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ChargeInfoJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ChargeApi chargeApi;

    private final ChargeRepository chargeRepository;

    private final EntityManagerFactory emf;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("saveChargeDataFromApiJob")
                .incrementer(new RunIdIncrementer())
                .start(saveChargeDataFromApi())
                .next(changeMembershipTest())
                .build();
    }

    @Bean
    public Step saveChargeDataFromApi() {
        return this.stepBuilderFactory.get("saveChargeDataFromApiStep")
                .<ChargeApiDto, Charge>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<ChargeApiDto> reader() {
        List<ChargeApiDto> chargeDtoList = null;
        try {
            ResponseEntity<String> response = chargeApi.getChargeResponseEntity();
            chargeDtoList = chargeApi.getChargeDtoList(response);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new ListItemReader<>(chargeDtoList);
    }

    @Bean
    public ItemProcessor<ChargeApiDto, Charge> processor() {
        return item -> {
            Optional<Charge> byId = chargeRepository.findByChargePlaceId(item.getChargePlaceId());

            if (byId.isEmpty()) {
                return item.toEntity();
            } else {
                Charge charge = byId.get();
                charge.updateCounts(item.getTotalCount(), item.getCurrentCount());
                return charge;
            }
        };
    }

    @Bean
    public JpaItemWriter<Charge> writer() {
        return new JpaItemWriterBuilder<Charge>()
                .entityManagerFactory(emf)
                .usePersist(true)
                .build();
    }

    @Bean
    public Step changeMembershipTest() {
        return this.stepBuilderFactory.get("changeMembership Step")
                .tasklet((contribution, chunkContext) -> {
                    // TODO sql in 키워드에 해당하는 jpa 기능 찾아서 수정 필요
                    chargeRepository.findByName("오곡").forEach(e -> e.setMembership(ChargePlaceMembership.MEMBERSHIP));
                    chargeRepository.findByName("국회").forEach(e -> e.setMembership(ChargePlaceMembership.MEMBERSHIP));
                    return RepeatStatus.FINISHED;
                }).build();
    }
}









