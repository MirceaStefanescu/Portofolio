package com.portfolio.securerag;

import com.portfolio.securerag.service.Chunker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChunkerTest {

    @Test
    void createsChunksWithinSize() {
        Chunker chunker = new Chunker();
        String text = "This is a long text that should be split into smaller chunks for embedding and retrieval.".repeat(10);

        var chunks = chunker.chunk(text, 120, 20);

        assertFalse(chunks.isEmpty());
        for (String chunk : chunks) {
            assertTrue(chunk.length() <= 120);
        }
    }
}
