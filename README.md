## To download and generate zip
### In ProjectSettingsResource :

- for FrontEnd generate zip (angular)

        @GetMapping("/download/front/{id}")
            public byte[] generateFontZip(@PathVariable(name = "id") final Long id) throws IOException {
            logger.info("Generate front end zip file: {}", id);
            return generateFrontendZip.generateZip(id);
        }
  - for FrontEnd generate zip (angular)

          @GetMapping("/generalSettingsValues")
          public ResponseEntity<Map<Long, Long>> getGeneralSettingsValues() {
              return ResponseEntity.ok(generalSettingsRepository.findAll(Sort.by("id"))
                      .stream()
                      .collect(CustomCollectors.toSortedMap(GeneralSettings::getId, GeneralSettings::getId)));
          }
    - Injections

              private final IGenerateZip generateBackendZip;
              private final IGenerateZip generateFrontendZip;
        
              public ProjectSettingsResource(final ProjectSettingService projectSettingService,
                  final GeneralSettingsRepository generalSettingsRepository,
                  final DatabaseSettingsRepository databaseSettingsRepository,
                  final DeveloperPreferencesRepository developerPreferencesRepository,
                  @Qualifier("IGenerateBackendZipImpl") IGenerateZip generateBackendZip,
                  @Qualifier("IGenerateFrontendZipImpl") IGenerateZip generateFrontendZip
                ) {
  
                      this.projectSettingService = projectSettingService;
                      this.generalSettingsRepository = generalSettingsRepository;
                      this.databaseSettingsRepository = databaseSettingsRepository;
                      this.developerPreferencesRepository = developerPreferencesRepository;
                      this.generateBackendZip = generateBackendZip;
                      this.generateFrontendZip = generateFrontendZip;
              }
