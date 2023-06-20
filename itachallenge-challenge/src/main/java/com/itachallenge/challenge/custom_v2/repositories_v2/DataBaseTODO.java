package com.itachallenge.challenge.custom_v2.repositories_v2;

import com.itachallenge.challenge.documents.Challenge;
import reactor.core.publisher.Flux;

/**
 * Class for assume the logic for "joins". Because this logic depends
 * on the technology used for DB.
 */
public interface DataBaseTODO {

    /*
    TODO:
    Service(s) expects a Challenge document
    that stores ALL INFO of his related languages (ids + names).
    This INFO IS REQUIRED FOR POPULATE THE DTO CORRECTLY.
    This logic MUST NOT BE IN THE LOGIC LAYER (depends on the technology
    of the DB used).

    So... TODO: fix Challenge document
     */

    Flux<Challenge> findAllChallenges();

    /*
    TODO: IMPORTANT!!!!!!!!!!!!!!!!!!
    Challenge Document is bad implemented:
    a) attribute Set<String> languages; (line 29)
        It has been designed like it stores the NAME OF THE LANGUAGE
            (see ChallengeRepositoryTest @BeforeEach: constructor uses NAMES VALUES)
        But in mongodb-test-data/challenges.json it stores the ID (integer) OF THE LANGUAGE
        TODO: -> Decision: WHAT SHOULD BE FIXED???
    b) When returning a ChallengeDto it includes the LanguageDto (id + name)
        -> converter needs BOTH info to create the dto
        -> So: converter implementation needs:
           Option A: challenge document with both info
           Option B: challenge document + N languages documents (to obtain the ids)
           TODO: -> Decision: Converter implementation depends on the decision
     c) If field is for ids -> TODO: fix ChallengeRepositoryTest
        If field is for name -> TODO: add findByName(String name) method in LanguageRepository + fix json
     d) Independent of the decision:
     if challenge document DON'T HAVE A FIELD @Transient Set<Language> languagesDocuments
     && DON'T EXIST ANY CLASS AS ADAPTER BETWEEN ANY SERVICE AND REPOSITORIES
     = Services are obligated to execute logic which MUST BE IN THE DATA LAYER.
     (repoA.find + repoB.find + join the data)
     *** Data layer SHOULD provide to Services, when FindX, ONE SINGLE OBJECT STORING ALL THE DATA NEEDED.

     How Converter can create a ChallengeDto from a Challenge, if the document does not store both
     (language id + language name)?
     A method like 'ChallengeDto convertDocumentsToChallengeDto(Challenge, Set<Language>)' DOES NOT MAKE SENSE,
     EXISTS A RELATION BETWEEN BOTH DOCUMENTS (N:N) -> Must exists:
        option 1) Challenge with dependency to Language
        option 2) Language with dependency to Challenge
        option 3) Foo.class with dependency to Challenge and Language

     TODO: fix rest layer. Logic in services can't be implemented till then....
     e) The all args constructor:
        When a new object is created: date is .now (before persisted) -> OK
        When an object is read from DB: date is replaced by .now -> wrong
        *** MongoRepository uses the all args constructor to populate the fields -> TODO: fix constructor
        //TODO: create in respositoryTest @Tests that checks:
           create object to save -> save creation date in a var -> save object -> load by id -> ALL FIELDS are equals (origin, loaded)
           -> change some field -> save -> load by id -> assert creation date remains equals as stored in var
     */
}
