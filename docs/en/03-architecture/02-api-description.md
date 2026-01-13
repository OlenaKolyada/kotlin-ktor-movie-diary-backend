# API Description

## 1. Entities

### Entry

| Field | Business Description | Example Value |
|-------|---------------------|---------------|
| id | Unique entry identifier | "446655440000" |
| userId | User identifier (entry owner) | "user:12345" |
| movieId | Movie identifier from database | "movie:tt0111161" |
| viewingDate | Viewing date | "2025-10-15" |
| rating | Movie rating (1-10) | 9 |
| comment | User comment | "Amazing film about hope" |
| createdAt | Entry creation date | "2025-10-15T20:30:00Z" |
| updatedAt | Last update date | "2025-10-16T10:00:00Z" |

### Movie

| Field       | Business Description    | Example Value                         |
|-------------|-------------------------|---------------------------------------|
| id          | Unique movie identifier | "movie:tt0111161"                     |
| title       | Movie title             | "The Shawshank Redemption"            |
| releaseYear | Release year            | 1994                                  | |
| genre       | Genres                  | ["Drama", "Crime"]                    |
| synopsis    | Synopsis                | "A banker is wrongfully convicted..." |
| posterUrl   | Movie poster URL        | "https://image.tmdb.org/..."          |
| tmdbId      | TMDb movie ID           | "278"                                 |

## 2. Business Functions (Methods)

### CRUDS for Entries

1. **`entry.create`** - Create a new viewing entry
    * Business rule: Only authenticated users can create entries
    * Constraints:
        * Required fields: movieId, viewingDate
        * Rating must be in range 1-10
        * Viewing date cannot be in the future
    * Validation: Check movie existence in database

2. **`entry.read`** - Get entry information
   * Business rule: User can view only their own entries
   * Returns: Complete entry information with movie data

3. **`entry.update`** - Update an existing entry
    * Business rule: Only owner can modify their entry
    * Constraints: Cannot change userId and movieId
    * Validation: Access rights verification

4. **`entry.delete`** - Delete an entry
    * Business rule: Only owner can delete their entry
    * Returns: Successful deletion status

5. **`entry.search`** - Search user's entries by criteria
   * Business logic: Returns current user's entries with applied filters
   * Filters:
      - By specific movie (movieId)
      - By viewing date range (dateFrom, dateTo)
      - By rating
   * Sorting: By viewing date (default: newest to oldest), by rating
   * Returns: List of entries matching search criteria
   * Usage: Universal method for getting entry lists and repeat viewing history

### Methods for Movies

6. **`movie.search`** - Search and automatic movie addition
   * Business logic: Search movies by title first in local database, if not found - query TMDb API with automatic saving of found results to local database
   * Filters: By title, release year, genre
   * Returns: List of movies with basic information
   * Note: Method performs implicit movie record creation (implicit create)

7. **`movie.read`** - Get detailed movie information
   * Business logic: Returns complete movie information from local database
   * Returns: Detailed movie information

**Note:** Full CRUD for movies (create, update, delete for administrators) is planned for implementation in future project iterations.

## 3. Business Rules

1. **Access Rights:**
    * Users see only their own entries
    * Administrators have access to all data for moderation
    * Guest access is forbidden - authentication required

2. **Data Validation:**
    * Rating: integer from 1 to 10
    * Viewing date: not later than current date
    * Comment: maximum 2000 characters
    * Movie title: required field when creating via API

3. **User Limitations:**
    * 60 requests per minute limit for movie search API (to protect against TMDb API abuse)
    * 100 requests per minute limit for other endpoints

4. **Working with Movies:**
    * Movies are automatically saved to local database on first search through TMDb
    * Search checks local database first, then TMDb
    * All movies are stored in local database for fast access
    * Movie information updates are only possible by administrators

## 4. Business Success Metrics

1. **API Response Speed:** 95% of requests should execute in < 500 ms
2. **Availability:** API available 99% of the time
3. **Search Accuracy:** At least 90% relevant results in first 10 search results
4. **Limitations:**
    * Regular users: 100 requests/minute
    * Administrators: no limitations