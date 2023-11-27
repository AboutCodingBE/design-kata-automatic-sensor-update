package be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain;

public class SemanticVersion {

    private int major = 0;
    private int minor = 0;
    private int patch = 0;
    private int revision = 0;
    private String rawVersion;

    public SemanticVersion(String version) {
        this.rawVersion = version;
        parse();
    }

    public void parse() {
        if (this.rawVersion != null && !this.rawVersion.isEmpty()) {
            String[] versionParts = rawVersion.split("\\.");
            try {
                this.major = Integer.parseInt(versionParts[0]);
                this.minor = Integer.parseInt(versionParts[1]);
                String[] patchRevision = versionParts[2].split("Rev");
                this.patch = Integer.parseInt(patchRevision[0]);

                if (patchRevision.length == 2) {
                    this.revision = Integer.parseInt(patchRevision[1]);
                }
            } catch (NumberFormatException formatException) {
                this.major = 0;
                this.minor = 0;
                this.patch = 0;
                this.revision = 0;
            }
        }
    }

    public boolean isEqualOrLargerThan(SemanticVersion other) {
        if (other.major == this.major &&
                other.minor == this.minor &&
                other.patch == this.patch &&
                other.revision == this.revision) {
            return true;
        }
        if (this.major > other.major) {
            return true;
        }
        if (this.major == other.major && this.minor > other.minor) {
            return true;
        }
        if (this.major == other.major && this.minor == other.minor && this.patch > other.patch) {
            return true;
        } else return this.major == other.major && this.minor == other.minor && this.patch == other.patch &&
                this.revision > other.revision;
    }
}
