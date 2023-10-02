package dev.luzifer.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"dev.luzifer.data.access"})
public class ApplicationConfiguration {
}
