package com.library.management.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.extensions.spring.converter.ConversionServiceAdapter;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ConversionServiceAdapterTest {

  @Mock
  private ConversionService conversionService;
  private ConversionServiceAdapter conversionServiceAdapter;

  @BeforeEach
  void init() {
    conversionServiceAdapter = new ConversionServiceAdapter(conversionService);
  }

  @Test
  void shouldCreateConversionAdapter() {
    assertNotNull(conversionServiceAdapter);
  }

}
