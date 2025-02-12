package io.aryby.spring_boot_crud.generator.frontend.implimentations;

import io.aryby.spring_boot_crud.generator.frontend.IAngularJson;
import org.springframework.stereotype.Service;

@Service
public class IAngularJsonImpl implements IAngularJson {
    @Override
    public String generateAngularJson(Long projectId) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
            {
                "$schema": "./node_modules/@angular/cli/lib/config/schema.json",
                "version": 1,
                "newProjectRoot": "projects",
                "projects": {
                    "angular-generator": {
                        "projectType": "application",
                        "schematics": {
                            "@schematics/angular:application": {
                                "strict": true
                            }
                        },
                        "root": "",
                        "sourceRoot": "src",
                        "prefix": "app",
                        "architect": {
                            "build": {
                                "builder": "@angular-devkit/build-angular:browser-esbuild",
                                "options": {
                                    "outputPath": "dist",
                                    "index": "src/index.html",
                                    "main": "src/main.ts",
                                    "polyfills": [
                                      "src/polyfills.ts",
                                      "@angular/localize/init"
                                    ],
                                    "tsConfig": "tsconfig.app.json",
                                    "assets": ["src/assets", "src/demo-prepare.html"],
                                    "styles": ["src/assets/css/app.css"],
                                    "scripts": [],
                                    "allowedCommonJsDependencies": []
                                },
                                "configurations": {
                                    "production": {
                                        "budgets": [
                                            {
                                                "type": "initial",
                                                "maximumWarning": "5mb",
                                                "maximumError": "10mb"
                                            },
                                            {
                                                "type": "anyComponentStyle",
                                                "maximumWarning": "2kb",
                                                "maximumError": "4kb"
                                            }
                                        ],
                                        "fileReplacements": [
                                            {
                                                "replace": "src/environments/environment.ts",
                                                "with": "src/environments/environment.prod.ts"
                                            }
                                        ],
                                        "outputHashing": "all"
                                    },
                                    "development": {
                                        "buildOptimizer": false,
                                        "optimization": false,
                                        "extractLicenses": false,
                                        "sourceMap": true,
                                        "namedChunks": true
                                    }
                                },
                                "defaultConfiguration": "production"
                            },
                            "serve": {
                                "builder": "@angular-devkit/build-angular:dev-server",
                                "configurations": {
                                    "production": {
                                        "buildTarget": "angular-generator:build:production"
                                    },
                                    "development": {
                                        "buildTarget": "angular-generator:build:development"
                                    }
                                },
                                "defaultConfiguration": "development"
                            },
                            "extract-i18n": {
                                "builder": "@angular-devkit/build-angular:extract-i18n",
                                "options": {
                                    "buildTarget": "angular-generator:build"
                                }
                            },
                            "test": {
                                "builder": "@angular-devkit/build-angular:karma",
                                "options": {
                                    "polyfills": [
                                      "src/polyfills.ts",
                                      "@angular/localize/init"
                                    ],
                                    "tsConfig": "tsconfig.spec.json",
                                    "assets": ["src/favicon.ico", "src/assets"],
                                    "styles": ["src/styles.css"],
                                    "scripts": []
                                }
                            }
                        }
                    }
                },
                "cli": {
                  "analytics": false
                }
            }


            """);

        return sb.toString();
    }
}
