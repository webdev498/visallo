module.exports = {
  title: 'Visallo',
  description: 'Visallo, a big data analysis and visualization platform to help analysts and investigators solve ambiguous problems.',
  gitbook: '3.x.x',
  language: 'en',
  direction: 'ltr',
  plugins: [ 'ga', 'theme-default' ],
  styles: {
      website: 'styles/website.css'
  },
  pluginsConfig: {
    ga: {
      token: 'UA-63006144-4'
    }
  },
  search: {
    maxIndexSize: 1000000000
  },
  variables: {
    v: {
      javaMajor: '8',
      javaExact: '8u51-b16',
      maven: '3.3.3',
      nodejs: 'v5.3.0',
      npm: '3.3.12',
      chrome: '<i>latest</i>',
      firefox: '38',
      ie: '11'
    }
  }
};
