package org.sonatype.nexus.plugin.migration.artifactory.util;

import java.util.List;

/**
 * Convert artifactory style pattern to nexus style pattern (which is regular expression)
 * 
 * @author Juven Xu
 */
public class PatternConvertor
{
    public static String convert125Pattern( String path )
    {
        if ( path.equals( "ANY" ) )
        {
            return ".*";
        }
        return path + ".*";
    }

    /**
     * Ant style (*, **, ?)
     * 
     * @param includes
     * @param excludes
     * @return
     */
    public static String convert130Pattern( List<String> includes, List<String> excludes )
    {
        // default is all
        if ( includes.isEmpty() )
        {
            includes.add( "**" );
        }

        StringBuffer regx = new StringBuffer();

        for ( int i = 0; i < includes.size(); i++ )
        {
            if ( i > 0 )
            {
                regx.append( "|" );
            }

            regx.append( "(" + convertAntStylePattern( includes.get( i ) ) + ")" );
        }
        
        // TODO: how to append excludes?

        return regx.toString();
    }

    /**
     *  ?   -> .{1} </br>
     *  **  -> .*   </br>
     *  *   -> [^/]* 
     * 
     * @param pattern
     * @return
     */
    public static String convertAntStylePattern( String pattern )
    {
        StringBuffer regx = new StringBuffer();

        for ( int i = 0; i < pattern.length(); )
        {

            if ( pattern.charAt( i ) == '?' )
            {
                regx.append( ".{1}" );

                i++;
            }
            else if ( pattern.charAt( i ) == '*' && ( i + 1 ) != pattern.length() && pattern.charAt( i + 1 ) == '*' )
            {
                regx.append( ".*" );

                i += 2;
            }
            else if ( pattern.charAt( i ) == '*' )
            {
                regx.append( "[^/]*" );

                i++;
            }
            else
            {
                regx.append( pattern.charAt( i ) );

                i++;
            }
        }

        return regx.toString();
    }
}
