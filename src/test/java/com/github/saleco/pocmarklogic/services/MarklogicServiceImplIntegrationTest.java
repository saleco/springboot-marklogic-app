package com.github.saleco.pocmarklogic.services;

import com.github.saleco.pocmarklogic.model.SampleDocument;
import com.github.saleco.pocmarklogic.model.SampleDocumentSearchResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MarklogicServiceImplIntegrationTest {

    @Autowired
    private MarklogicService marklogicService;

    @DisplayName("Create a document, fetch document and check the fields. Deleting created object after test.")
    @Test
    void addSampleDocumentAndFetchDocumentById_ThenShouldReturnCreatedDocument(){
        Long id = Long.valueOf(new Random().nextInt(100));
        createSampleDocument(id, null);

        SampleDocument sampleDocumentCheck = getById(id);

        assertAll(
          () -> assertNotNull(sampleDocumentCheck),
          () -> assertEquals(id, sampleDocumentCheck.getId()),
          () -> assertEquals("sample document", sampleDocumentCheck.getName())
        );

        deleteCreatedDocument(id);
    }

    @DisplayName("Create multiple documents, call count and then check document count.")
    @Test
    void addMultipleSampleDocumentAndGetCount_ThenCheckCountIsCorrect(){

        Long firstDocId = Long.valueOf(new Random().nextInt(100));
        createSampleDocument(firstDocId, null);

        Long secondDocId = Long.valueOf(new Random().nextInt(100));
        createSampleDocument(secondDocId, null);

        Long docsCount = getCount();

        assertEquals(2L, docsCount);

        deleteCreatedDocument(firstDocId);
        deleteCreatedDocument(secondDocId);
    }

    @DisplayName("Create sample document, search by name and then check Document Search Results.")
    @Test
    void addSampleDocumentAndSearchByName_ThenCheckReturnSampleDocumentSearchResult(){

        Long firstDocId = Long.valueOf(new Random().nextInt(100));
        createSampleDocument(firstDocId, "first document");

        Long secondDocId = Long.valueOf(new Random().nextInt(100));
        createSampleDocument(secondDocId, "second document");

        Long thirdDocId = Long.valueOf(new Random().nextInt(100));
        createSampleDocument(thirdDocId, "third document");

        SampleDocumentSearchResult sampleDocumentSearchResultCheck = findByName("document");

        assertAll(
          () -> assertNotNull(sampleDocumentSearchResultCheck),
          () -> assertFalse(sampleDocumentSearchResultCheck.getSampleDocuments().isEmpty()),
          () -> assertEquals(3, sampleDocumentSearchResultCheck.getSampleDocuments().size())
        );

        SampleDocumentSearchResult sampleDocumentSearchResultSecondCheck = findByName("first");
        assertAll(
          () -> assertNotNull(sampleDocumentSearchResultSecondCheck),
          () -> assertFalse(sampleDocumentSearchResultSecondCheck.getSampleDocuments().isEmpty()),
          () -> assertEquals(1, sampleDocumentSearchResultSecondCheck.getSampleDocuments().size())
        );

        deleteCreatedDocument(firstDocId);
        deleteCreatedDocument(secondDocId);
        deleteCreatedDocument(thirdDocId);
    }

    @DisplayName("Create sample documents, search all paginated and then check Document Search Results.")
    @Test
    void addSampleDocumentAndSearchAllPaginated_ThenCheckReturnSampleDocumentSearchResult(){

        Long firstDocId = Long.valueOf(new Random().nextInt(100));
        createSampleDocument(firstDocId, "first document");

        Long secondDocId = Long.valueOf(new Random().nextInt(100));
        createSampleDocument(secondDocId, "second document");

        Long thirdDocId = Long.valueOf(new Random().nextInt(100));
        createSampleDocument(thirdDocId, "third document");

        SampleDocumentSearchResult sampleDocumentSearchResultCheck = findAll(0, 2);

        assertAll("Check returned list paginated with 2 elements in the first page",
          () -> assertNotNull(sampleDocumentSearchResultCheck),
          () -> assertFalse(sampleDocumentSearchResultCheck.getSampleDocuments().isEmpty()),
          () -> assertEquals(2, sampleDocumentSearchResultCheck.getSampleDocuments().size())
        );

        SampleDocumentSearchResult sampleDocumentSearchResultSecondCheck= findAll(1, 2);
        assertAll("Check returned list paginated with 1 element in the second page",
          () -> assertNotNull(sampleDocumentSearchResultSecondCheck),
          () -> assertFalse(sampleDocumentSearchResultSecondCheck.getSampleDocuments().isEmpty()),
          () -> assertEquals(1, sampleDocumentSearchResultSecondCheck.getSampleDocuments().size())
        );

        deleteCreatedDocument(firstDocId);
        deleteCreatedDocument(secondDocId);
        deleteCreatedDocument(thirdDocId);
    }

    private SampleDocumentSearchResult findByName(String name) {
        return marklogicService.findByName(name);
    }

    private SampleDocumentSearchResult findAll(int page, int size){
        return marklogicService.findAll(page, size);
    }

    private Long getCount() {
        return marklogicService.count();
    }

    private void deleteCreatedDocument(Long id) {
        marklogicService.remove(id);
    }

    private SampleDocument getById(Long id) {
        return marklogicService.findById(id);
    }

    private void createSampleDocument(Long id, String name) {
        if(id == null) id = Long.valueOf(new Random().nextInt(100));
        if(name == null) name = "sample document";

        marklogicService
          .addSampleDocument(SampleDocument.builder()
            .id(id)
            .name(name)
            .build());
    }

}