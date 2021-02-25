package com.github.saleco.pocmarklogic.services;

import com.github.saleco.pocmarklogic.model.SampleDocumentSearchResult;
import com.github.saleco.pocmarklogic.model.SampleDocument;

public interface MarklogicService {
    void addSampleDocument(SampleDocument sampleDocument);
    void remove(Long id);
    SampleDocumentSearchResult findAll(int page, int pageSize);
    SampleDocument findById(Long id);
    Long count();
    SampleDocumentSearchResult findByName(String name);
}
