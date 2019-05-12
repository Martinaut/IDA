/**
 * The data library provides access to data stored in a GraphDB repository.
 * The connection to the GraphDB can be configured by setting the appropriate values in {@code application.properties}.
 * <p>
 * Following properties have to be set:
 * <ul>
 * <li><b>graphdb.remote.server-url</b>: URL to the server</li>
 * <li><b>graphdb.remote.repository-id</b>: The ID of the repository.</li>
 * </ul>
 * or
 * <ul>
 * <li><b>graphdb.embedded.directory</b>: Path to the repository of the db</li>
 * <li><b>graphdb.embedded.data-directory</b>: Path to the directory with the data files</li>
 * <li><b>graphdb.embedded.index-directory</b>: Path to the directory with the index creation files</li>
 * <li><b>graphdb.embedded.materialization-directory</b>: Path to the directory with the materialization query files</li>
 * <li><b>graphdb.embedded.wordnet-files</b>: Paths to the wordnet files</li>
 * </ul>
 * <p>
 * Either remote or embedded as to be null; the otherone must not be null.
 */
package at.jku.dke.ida.data;