package com.github.saleco.pocmarklogic.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.saleco.pocmarklogic.model.SampleDocumentSearchResult;
import com.github.saleco.pocmarklogic.model.SampleDocument;
import com.marklogic.client.document.JSONDocumentManager;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.JacksonHandle;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.io.StringHandle;
import com.marklogic.client.query.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MarklogicServiceImpl implements MarklogicService {

    public static final String SAMPLE_DOCUMENT_COLLECTION = "/sample-documents.json";
    private static final int PAGE_SIZE = 20;

    private final QueryManager queryManager;
    private final JSONDocumentManager jsonDocumentManager;

    @Override
    public SampleDocumentSearchResult findAll(int page, int pageSize) {
        StringQueryDefinition query = queryManager.newStringDefinition();
        query.setCollections(SAMPLE_DOCUMENT_COLLECTION);

        SearchHandle resultsHandle = new SearchHandle();
        queryManager.setPageLength(pageSize);

        int paginationStart = (page * pageSize) + 1;

        queryManager.search(query, resultsHandle, paginationStart);
        return toSearchResult(resultsHandle);

    }

    @Override
    public SampleDocument findById(Long id) {
        JacksonHandle jacksonHandle = new JacksonHandle();
        jsonDocumentManager.read(getId(id), jacksonHandle);
        return fetchSampleDocument(jacksonHandle);
    }

    @Override
    public SampleDocumentSearchResult findByName(String name) {
        StringQueryDefinition query = queryManager.newStringDefinition();
        query.setCriteria(name);
        query.setCollections(SAMPLE_DOCUMENT_COLLECTION);

        queryManager.setPageLength(PAGE_SIZE);
        SearchHandle resultsHandle = new SearchHandle();
        queryManager.search(query, resultsHandle);

        return toSearchResult(resultsHandle);

    }

    @Override
    public void addSampleDocument(SampleDocument sampleDocument) {
        DocumentMetadataHandle metadata = new DocumentMetadataHandle();
        metadata.getCollections().add(SAMPLE_DOCUMENT_COLLECTION);

        JacksonHandle writeHandle = new JacksonHandle();
        JsonNode writeDocument = writeHandle.getMapper().convertValue(sampleDocument, JsonNode.class);
        writeHandle.set(writeDocument);

        StringHandle stringHandle = new StringHandle(writeDocument.toString());
        jsonDocumentManager.write(getId(sampleDocument.getId()), metadata, stringHandle);
    }

    @Override
    public void remove(Long id) {
        jsonDocumentManager.delete(getId(id));
    }

    @Override
    public Long count() {
        StructuredQueryBuilder sb = queryManager.newStructuredQueryBuilder();
        StructuredQueryDefinition criteria = sb.collection(SAMPLE_DOCUMENT_COLLECTION);
        SearchHandle resultsHandle = new SearchHandle();
        queryManager.search(criteria, resultsHandle);

        return resultsHandle.getTotalResults();
    }

    private String getId(Long sku) {
        return String.format("/sample-documents/%d.json", sku);
    }

    private SampleDocument fetchSampleDocument(JacksonHandle jacksonHandle) {
        try {
            JsonNode jsonNode = jacksonHandle.get();
            return jacksonHandle.getMapper().readValue(jsonNode.toString(), SampleDocument.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to cast to sample document", e);
        }
    }

    private SampleDocumentSearchResult toSearchResult(SearchHandle resultsHandle) {
        List<SampleDocument> sampleDocuments = new ArrayList<>();

        for (MatchDocumentSummary summary : resultsHandle.getMatchResults()) {
            JacksonHandle jacksonHandle = new JacksonHandle();
            jsonDocumentManager.read(summary.getUri(), jacksonHandle);
            sampleDocuments.add(fetchSampleDocument(jacksonHandle));
        }
        return SampleDocumentSearchResult.builder()
            .sampleDocuments(sampleDocuments)
            .build();
    }


}
