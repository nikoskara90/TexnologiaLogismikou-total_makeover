package com.icsd19080_icsd19235.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icsd19080_icsd19235.model.Paper.PaperState;
import com.icsd19080_icsd19235.model.Paper;
import com.icsd19080_icsd19235.model.Conference;

import java.util.List;

@Repository
public interface PaperRepository extends JpaRepository<Paper, Long> {
    List<Paper> findByTitleContainingAndAuthorNamesContainingAndAbstractTextContainingOrderByTitle(String title, String authorNames, String abstractText);

    List<Paper> findByTitleContainingAndAuthorNamesContainingOrderByTitle(String title, String authorNames);

    List<Paper> findByAuthorNamesContainingAndAbstractTextContainingOrderByTitle(String authorNames, String abstractText);

    List<Paper> findByTitleContainingAndAbstractTextContainingOrderByTitle(String title, String abstractText);

    List<Paper> findByTitleContainingOrderByTitle(String title);

    List<Paper> findByAuthorNamesContainingOrderByTitle(String authorNames);

    List<Paper> findByAbstractTextContainingOrderByTitle(String abstractText);

    List<Paper> findAllByOrderByTitle();

    List<Paper> findByState(PaperState state);

    List<Paper> findByConferenceAndState(Conference conference, PaperState state);
}

