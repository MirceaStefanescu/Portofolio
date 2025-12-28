package com.portfolio.securerag;

import com.portfolio.securerag.config.RagProperties;
import com.portfolio.securerag.service.HashEmbeddingService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HashEmbeddingServiceTest {

    @Test
    void embedsDeterministically() {
        RagProperties props = new RagProperties();
        props.setVectorSize(32);
        HashEmbeddingService service = new HashEmbeddingService(props);

        float[] first = service.embed("Secure RAG services");
        float[] second = service.embed("Secure RAG services");

        assertEquals(32, first.length);
        assertArrayEquals(first, second);
    }
}
